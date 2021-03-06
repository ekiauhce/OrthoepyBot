package com.github.ekiauhce.orthoepybot.controllers;

import com.github.ekiauhce.orthoepybot.entities.Player;
import com.github.ekiauhce.orthoepybot.services.OrthoepyService;
import com.github.ekiauhce.orthoepybot.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
import java.util.function.Predicate;

@Component
public class UserActionsControllerImpl implements UserActionsController {
    @Autowired
    private OrthoepyService orthoepyService;

    @Override
    public SendMessage start(User user) {
        Player player = orthoepyService.getPlayer(user);
        orthoepyService.savePlayer(player);
        return SendMessage.builder()
                .chatId(user.getId().toString())
                .text(START_TEXT)
                .build();
    }

    @Override
    public SendMessage practice(User user) {
        Player player = orthoepyService.getPlayer(user);
        orthoepyService.beginPractice(player);
        List<String> buttonNames = orthoepyService.getShuffledWordForms(player);
        orthoepyService.savePlayer(player);
        return SendMessage.builder()
                .chatId(user.getId().toString())
                .text(PRACTICE_TEXT)
                .replyMarkup(Utils.getOneRowKeyboard(buttonNames))
                .build();
    }

    @Override
    public SendMessage onCorrectAnswer(User user) {
        Player player = orthoepyService.getPlayer(user);
        orthoepyService.increaseScore(player);
        orthoepyService.setRandomWord(player);
        List<String> buttonNames = orthoepyService.getShuffledWordForms(player);
        orthoepyService.savePlayer(player);
        return SendMessage.builder()
                .chatId(user.getId().toString())
                .text("✅ Верно!")
                .replyMarkup(Utils.getOneRowKeyboard(buttonNames))
                .build();
    }

    @Override
    public SendMessage onWrongAnswer(User user) {
        Player player = orthoepyService.getPlayer(user);
        orthoepyService.endPractice(player);
        String format = orthoepyService.isNewHighScore(player) ? NEW_HS_FORMAT : BASIC_FORMAT;
        Integer score = orthoepyService.getScore(player);
        orthoepyService.summarizeScore(player);
        orthoepyService.savePlayer(player);
        return SendMessage.builder()
                .chatId(user.getId().toString())
                .text(String.format(format, score))
                .parseMode(ParseMode.HTML)
                .replyMarkup(Utils.getKeyboardRemove())
                .build();
    }

    @Override
    public Predicate<Update> filterIsPracticingTrue() {
        return upd -> {
            User user = upd.getMessage().getFrom();
            Player player = orthoepyService.getPlayer(user);
            return orthoepyService.getIsPracticing(player);
        };
    }

    @Override
    public Predicate<Update> filterIsCorrectAnswer() {
        return upd -> {
            Message msg = upd.getMessage();
            Player player = orthoepyService.getPlayer(msg.getFrom());
            String correct = orthoepyService.getCorrectForm(player);
            return msg.getText().equals(correct);
        };
    }

    @Override
    public Predicate<Update> filterIsWrongAnswer() {
        return upd -> {
            Message msg = upd.getMessage();
            Player player = orthoepyService.getPlayer(msg.getFrom());
            String correct = orthoepyService.getWrongForm(player);
            return msg.getText().equals(correct);
        };
    }

    @Override
    public SendMessage leaderboard(User user) {
        Player player = orthoepyService.getPlayer(user);
        String leaderboardText = orthoepyService.getLeaderboardText();
        Integer index = orthoepyService.getIndexInRating(player);
        String placeText = index != -1 ? String.format(PLACE_FORMAT, index + 1) : "";
        return SendMessage.builder()
                .chatId(user.getId().toString())
                .text("\uD83C\uDFC6 Таблица лидеров:\n" + leaderboardText + placeText)
                .parseMode(ParseMode.HTML)
                .build();
    }

    @Override
    public SendMessage mistakes(User user) {
        Player player = orthoepyService.getPlayer(user);
        String myMistakesText = orthoepyService.getMyMistakesText(player);
        return SendMessage.builder()
                .chatId(user.getId().toString())
                .text("\uD83D\uDCCB Мои ошибки:\n" + myMistakesText)
                .parseMode(ParseMode.HTML)
                .build();
    }

    @Override
    public SendMessage hardest(User user) {
        String hardestWordsText = orthoepyService.getHardestWordsText();
        return SendMessage.builder()
                .chatId(user.getId().toString())
                .text("\uD83E\uDD2F Самые сложные слова:\n" + hardestWordsText)
                .parseMode(ParseMode.HTML)
                .build();
    }

    @Override
    public SendMessage help(User user) {
        return SendMessage.builder()
                .chatId(user.getId().toString())
                .text(HELP_TEXT)
                .build();
    }

    private static final String START_TEXT =
            "▶️ Введите /practice, чтобы начать практику\n" +
            "❔ Введите /help, если возникли вопросы";
    private static final String PRACTICE_TEXT = "Выберите слово с правильным ударением:";
    private static final String BASIC_FORMAT =
            "❌ Не верно! Ваш счет: [ <b>%s</b> ]\n" +
            "↩️ Введите /practice, чтобы начать заново";
    private static final String NEW_HS_FORMAT =
            "❌ Не верно! Ваш счет: [ <b>%s</b> ]\n" +
            "\uD83C\uDF89 Это новый рекорд!\n" +
            "↩️ Введите /practice, чтобы начать заново";
    private static final String PLACE_FORMAT = "\nВаше место в рейтинге: [ <b>%s</b> ]";
    private static final String HELP_TEXT =
            "❓ Как посмотреть список доступных комманд?\n" +
            "\uD83D\uDCA1 /commands\n" +
            "\n" +
            "❓ Есть ли рейтинговая система?\n" +
            "\uD83D\uDCA1 Да, /leaderboard\n" +
            "\n" +
            "❓ Как посмотреть список своих ошибок?\n" +
            "\uD83D\uDCA1 /mistakes\n" +
            "\n" +
            "❓ Как посмотреть список самых сложных слов?\n" +
            "\uD83D\uDCA1 /hardest\n" +
            "\n" +
            "❓ Как изменить никнейм в таблице лидеров?\n" +
            "\uD83D\uDCA1 Он меняется автоматически, при вводе любой команды\n" +
            "\n" +
            "❓ Остались вопросы?\n" +
            "\uD83D\uDCA1 Напиши мне в личку, @ekiauhce\n";
}
