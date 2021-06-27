package com.sbo.bot.events;

import com.sbo.common.CreationEvent;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


public class SendMessageCreationEvent extends CreationEvent<SendMessage> {
    private SendMessageCreationEvent(SendMessage object) {
        super(object);
    }

    public static SendMessageCreationEvent of(SendMessage object) {
        return new SendMessageCreationEvent(object);
    }
}
