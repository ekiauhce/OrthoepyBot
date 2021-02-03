package com.github.ekiauhce.orthoepybot.bot;

import com.github.ekiauhce.orthoepybot.controllers.UserActionsController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.abilitybots.api.util.AbilityUtils.getUser;

@Component
public class TelegramBot extends AbilityBot {

    @Autowired
    UserActionsController userActionsController;

    /**
     * Responds to /start command
     * @see UserActionsController#start(User)
     */
    public Ability start() {
        return Ability.builder()
                .name("start")
                .info("Запустить бота")
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> {
                    SendMessage startMsg = userActionsController.start(ctx.user());
                    silent.execute(startMsg); })
                .build();
    }


    /**
     * Responds to /practice command
     * @see UserActionsController#practice(User)
     */
    public Ability practice() {
        return Ability.builder()
                .name("practice")
                .info("Начать практику")
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> {
                    SendMessage practiceMsg = userActionsController.practice(ctx.user());
                    silent.execute(practiceMsg); })
                .build();
    }

    /**
     * Responds to user's input, only if it is a text,
     * user is practicing
     * and input is correct answer
     * @see UserActionsController#onCorrectAnswer(User) 
     */
    public Reply replyOnCorrectAnswer() {
        return Reply.of(
                upd -> {
                    SendMessage onCorrectAnswerMsg = userActionsController.onCorrectAnswer(getUser(upd));
                    silent.execute(onCorrectAnswerMsg); },
                Flag.TEXT,
                userActionsController.filterIsPracticingTrue(),
                userActionsController.filterIsCorrectAnswer());
    }

    /**
     * Responds to user's input, only if it is a text,
     * user is practicing
     * and input is wrong answer
     * @see UserActionsController#onWrongAnswer(User) 
     */
    public Reply replyOnWrongAnswer() {
        return Reply.of(
                upd -> {
                    SendMessage onWrongAnswerMsg = userActionsController.onWrongAnswer(getUser(upd));
                    silent.execute(onWrongAnswerMsg); },
                Flag.TEXT,
                userActionsController.filterIsPracticingTrue(),
                userActionsController.filterIsWrongAnswer());
    }


    /**
     * Responds to /leaderboard command
     * @see UserActionsController#leaderboard(User) 
     */
     public Ability leaderboard() {
        return Ability.builder()
                .name("leaderboard")
                .info("Таблица лидеров")
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> {
                    SendMessage leaderboardMsg = userActionsController.leaderboard(ctx.user());
                    silent.execute(leaderboardMsg); })
                .build();
    }


    /**
     * Responds to /mistakes command
     * @see UserActionsController#mistakes(User) 
     */
    public Ability mistakes() {
        return Ability.builder()
                .name("mistakes")
                .info("Мои ошибки")
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> {
                    SendMessage mistakesMsg = userActionsController.mistakes(ctx.user());
                    silent.execute(mistakesMsg); })
                .build();
    }


    /**
     * Responds to /hardest command
     * @see UserActionsController#hardest(User)
     */
    public Ability hardest() {
        return Ability.builder()
                .name("hardest")
                .info("Самые сложные слова")
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> {
                    SendMessage commonMsg = userActionsController.hardest(ctx.user());
                    silent.execute(commonMsg); })
                .build();
    }

    @Value("${bot.creator-id}")
    private int creatorId;

    @Override
    public int creatorId() {
        return creatorId;
    }

    public TelegramBot(
            @Value("${bot.api-token}") String botToken,
            @Value("${bot.username}") String botUsername) {
        super(botToken, botUsername);
    }
}
