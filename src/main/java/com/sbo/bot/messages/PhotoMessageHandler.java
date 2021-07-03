package com.sbo.bot.messages;

import com.sbo.bot.messages.providers.PhotoDataProvider;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.function.Predicate;

/**
 * @author Dmitars
 */
public class PhotoMessageHandler extends MessageHandler {

    @Override
    protected PhotoDataProvider extractData(Message message) {
        return new PhotoDataProvider(message.getPhoto());
    }

    @Override
    protected Predicate<Message> getDataPredicate() {
        return Message::hasPhoto;
    }
}
