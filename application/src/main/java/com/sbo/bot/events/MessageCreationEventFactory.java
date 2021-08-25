package com.sbo.bot.events;

import com.sbo.common.CreationEvent;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * @author Dmitars
 */
public class MessageCreationEventFactory {
    public static CreationEvent<? extends BotApiMethod<Message>> create(BotApiMethod<Message> message) {
        if (message instanceof SendMessage)
            return SendMessageCreationEvent.of((SendMessage) message);
        else
            throw new RuntimeException("Unrecognizable message type");
    }
}
