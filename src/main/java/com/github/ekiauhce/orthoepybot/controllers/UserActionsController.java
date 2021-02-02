package com.github.ekiauhce.orthoepybot.controllers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.function.Predicate;


/**
 * Controller interface. Prepares data from business logic
 * to be sent via Telegram bot.
 */
public interface UserActionsController {
    SendMessage start(User user);
    SendMessage practice(User user);
    SendMessage onCorrectAnswer(User user);
    SendMessage onWrongAnswer(User user);
    Predicate<Update> filterIsPracticingTrue();
    Predicate<Update> filterIsCorrectAnswer();
    Predicate<Update> filterIsWrongAnswer();
    SendMessage leaderboard(User user);
    SendMessage mistakes(User user);
    SendMessage hardest(User user);
}
