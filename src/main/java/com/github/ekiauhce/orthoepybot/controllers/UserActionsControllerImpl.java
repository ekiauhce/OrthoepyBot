package com.github.ekiauhce.orthoepybot.controllers;

import com.github.ekiauhce.orthoepybot.entities.Player;
import com.github.ekiauhce.orthoepybot.services.OrthoepyService;
import com.github.ekiauhce.orthoepybot.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
                .parseMode(ParseMode.MARKDOWN)
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
                .text("Таблица лидеров:\n" + leaderboardText + placeText)
                .parseMode(ParseMode.MARKDOWN)
                .build();
    }

    @Override
    public SendMessage mistakes(User user) {
        Player player = orthoepyService.getPlayer(user);
        String myMistakesText = orthoepyService.getMyMistakesText(player);
        return SendMessage.builder()
                .chatId(user.getId().toString())
                .text("Мои ошибки:\n" + myMistakesText)
                .parseMode(ParseMode.MARKDOWN)
                .build();
    }

    @Override
    public SendMessage hardest(User user) {
        String hardestWordsText = orthoepyService.getHardestWordsText();
        return SendMessage.builder()
                .chatId(user.getId().toString())
                .text("Самые сложные слова:\n" + hardestWordsText)
                .parseMode(ParseMode.MARKDOWN)
                .build();
    }

    private static final String START_TEXT = "Введите /practice, чтобы начать практику";
    private static final String PRACTICE_TEXT = "Выберите слово с правильной постановкой ударения:";
    private static final String BASIC_FORMAT =
            "❌ Не верно! Ваш счет: \\[ *%s* ]. Введите /practice, чтобы начать заново";
    private static final String NEW_HS_FORMAT =
            "❌ Не верно! Ваш счет: \\[ *%s* ]. Поздравляю, это новый рекорд \uD83C\uDF89 " +
                    "Введите /practice, чтобы начать заново";
    private static final String PLACE_FORMAT = "\nВаше место в рейтинге: \\[ *%s* ]";
}
