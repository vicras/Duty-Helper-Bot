package com.sbo.bot.messages;

import com.sbo.bot.messages.providers.TextDataProvider;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.function.Predicate;

/**
 * @author Dmitars
 */
public class TextMessageHandler extends MessageHandler {

    @Override
    protected TextDataProvider extractData(Message message) {
        return new TextDataProvider(message.getText());
    }

    @Override
    public Predicate<Message> getDataPredicate() {
        return message -> message.hasText() && !message.getText().isBlank();
    }
}
