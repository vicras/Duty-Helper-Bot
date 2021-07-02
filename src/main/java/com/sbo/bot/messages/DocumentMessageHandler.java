package com.sbo.bot.messages;

import com.sbo.bot.messages.providers.DocumentDataProvider;
import com.sbo.bot.messages.providers.MessageDataProvider;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.function.Predicate;

/**
 * @author Dmitars
 */
public class DocumentMessageHandler extends MessageHandler{

    @Override
    protected DocumentDataProvider extractData(Message message) {
        return new DocumentDataProvider(message.getDocument());
    }

    @Override
    protected Predicate<Message> getDataPredicate() {
        return Message::hasDocument;
    }
}
