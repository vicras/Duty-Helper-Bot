package com.sbo.bot.builder;


import com.sbo.bot.enums.Command;
import com.sbo.entity.Person;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/**
 * MessageBuilder is used to build instances of {@link SendMessage}
 * <p>
 * MessageBuilder provides useful methods that simplify creation of bot replies
 */
public final class InlineMessageBuilder {
    private final StringBuilder sb = new StringBuilder();
    private final List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
    @Setter
    private String chatId;
    private List<InlineKeyboardButton> row = null;

    private InlineMessageBuilder() {
    }

    /**
     * Creates new MessageBuilder with initialized chatId
     *
     * @param chatId of user that will receive the message
     * @return instance of MessageBuilder with initialized chatId
     */
    public static InlineMessageBuilder builder(Long chatId) {
        InlineMessageBuilder builder = new InlineMessageBuilder();
        builder.setChatId(chatId.toString());
        return builder;
    }

    /**
     * Creates new MessageBuilder with initialized chatId
     *
     * @param user that will receive the message
     * @return instance of MessageBuilder with initialized chatId
     */
    public static InlineMessageBuilder builder(Person user) {
        return builder(user.getTelegramId());
    }

    public InlineMessageBuilder header(String text, Object... args) {
        sb.append("*");
        sb.append(String.format(text, args));
        sb.append("*");
        return line();
    }

    /**
     * Simplified use of {@link String#format(String, Object...) String.format} that adds new formatted line to the
     * inner instance of {@link StringBuilder}
     *
     * @param text first agrument of {@link String#format(String, Object...) String.format}
     * @param args second and following arguments of {@link String#format(String, Object...) String.format}
     * @return this
     */
    public InlineMessageBuilder line(String text, Object... args) {
        sb.append(String.format(text, args));
        return line();
    }

    /**
     * Creates new line break
     *
     * @return this
     */
    public InlineMessageBuilder line() {
        sb.append(String.format("%n"));
        return this;
    }

    /**
     * Creates new {@link InlineKeyboardButton} row
     *
     * @return this
     */
    public InlineMessageBuilder row() {
        addRowToKeyboard();
        row = new ArrayList<>();
        return this;
    }

    /**
     * Creates new {@link InlineKeyboardButton}
     *
     * @param text         button text
     * @param callbackData on click callback
     * @return this
     */
    public InlineMessageBuilder button(String text, String callbackData) {
        var button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        row.add(button);
        return this;
    }

    /**
     * Creates new {@link InlineKeyboardButton}
     *
     * @param text    button text
     * @param command on click callback
     * @return this
     */
    public InlineMessageBuilder button(String text, Command command) {
        return button(text, command.toString());
    }

    /**
     * Creates new {@link InlineKeyboardButton}
     *
     * @param text    button text (and callback argument)
     * @param command on click callback
     * @return this
     */
    public InlineMessageBuilder buttonWithArguments(String text, Command command) {
        return button(text, command.toString() + " " + text);
    }

    /**
     * Builds an instance of {@link SendMessage}
     *
     * @return {@link SendMessage}
     */
    public SendMessage build() {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        sendMessage.setText(sb.toString().replace("_", " "));

        addRowToKeyboard();

        if (!keyboard.isEmpty()) {
            var markup = new InlineKeyboardMarkup();
            markup.setKeyboard(keyboard);
            sendMessage.setReplyMarkup(markup);
        }

        return sendMessage;
    }

    /**
     * Adds new row to keyboard.
     * Performs null check of current row
     */
    private void addRowToKeyboard() {
        if (row != null) {
            keyboard.add(row);
        }
    }
}
