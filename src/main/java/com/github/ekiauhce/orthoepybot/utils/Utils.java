package com.github.ekiauhce.orthoepybot.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Utility class with stateless methods
 */
public class Utils {
    public static ReplyKeyboard getOneRowKeyboard(List<String> buttonNames) {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.addAll(buttonNames);
        return ReplyKeyboardMarkup.builder()
                .resizeKeyboard(true)
                .keyboardRow(keyboardRow)
                .build();
    }

    public static ReplyKeyboard getKeyboardRemove() {
        return ReplyKeyboardRemove.builder()
                .removeKeyboard(true)
                .build();
    }

    public static <E> String getTable(List<E> list, Function<E, String> mapper) {
        return IntStream.range(0, list.size()).boxed()
                .map(index -> String.format("%s. ", index + 1) + mapper.apply(list.get(index)))
                .collect(Collectors.joining("\n"));
    }
}
