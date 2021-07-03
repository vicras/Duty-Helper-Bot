package com.sbo.bot.events;

import com.sbo.common.CreationEvent;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


public class SendMessageCreationEvent extends CreationEvent<SendMessage> {
    private SendMessageCreationEvent(SendMessage object) {
        super(object);
    }

    public static SendMessageCreationEvent of(SendMessage object) {
        return new SendMessageCreationEvent(object);
    }
}
