package com.sbo.bot.builder;

import com.sbo.domain.postgres.entity.Person;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * @author viktar hraskou
 */
public class MessageBuilder {

    private final StringBuilder sb = new StringBuilder();
    private final List<KeyboardRow> keyboard = new ArrayList<>();
    private KeyboardRow row = null;
    @Setter
    private String chatId;

    private boolean isResizable = true;

    private boolean isOneTime = false;

    private boolean deleteKeyboard = false;

    public static MessageBuilder builder(Long chatId) {
        var builder = new MessageBuilder();
        builder.setChatId(chatId.toString());
        return builder;
    }

    public static MessageBuilder builder(Person person) {
        return builder(person.getTelegramId());
    }

    public MessageBuilder line(String text, Object... args) {
        sb.append(String.format(text, args));
        return line();
    }

    public MessageBuilder line() {
        sb.append(String.format("%n"));
        return this;
    }

    public MessageBuilder row() {
        addRowToKeyboard();
        row = new KeyboardRow();
        return this;
    }

    public MessageBuilder button(String text) {
        KeyboardButton button = KeyboardButton.builder()
                .text(text)
                .build();
        row.add(button);
        return this;
    }

    public MessageBuilder geoButton(String text) {
        KeyboardButton button = KeyboardButton.builder()
                .requestLocation(true)
                .text(text)
                .build();
        row.add(button);
        return this;
    }

    public MessageBuilder telButton(String text) {
        KeyboardButton button = KeyboardButton.builder()
                .requestContact(true)
                .text(text)
                .build();
        row.add(button);
        return this;
    }

    public MessageBuilder setResizable(boolean resizable) {
        isResizable = resizable;
        return this;
    }

    public MessageBuilder setOneTime(boolean oneTime) {
        isOneTime = oneTime;
        return this;
    }

    public MessageBuilder withKeyboardDelete() {
        deleteKeyboard = true;
        return this;
    }

    public SendMessage build() {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        sendMessage.setText(sb.toString().replace("_", " "));

        addRowToKeyboard();

        if (deleteKeyboard) {
            var keyboardRemove = ReplyKeyboardRemove.builder()
                    .removeKeyboard(true)
                    .build();
            sendMessage.setReplyMarkup(keyboardRemove);
            return sendMessage;
        }

        if (!keyboard.isEmpty()) {
            var markup = new ReplyKeyboardMarkup();

            markup.setKeyboard(keyboard);
            markup.setResizeKeyboard(isResizable);
            markup.setOneTimeKeyboard(isOneTime);

            sendMessage.setReplyMarkup(markup);
        }

        return sendMessage;
    }

    private void addRowToKeyboard() {
        if (row != null) {
            keyboard.add(row);
        }
    }
}
