package com.sbo.bot.state;

import com.sbo.bot.events.MessageCreationEventFactory;
import com.sbo.bot.events.SendMessageCreationEvent;
import com.sbo.common.CreationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitars
 */
@Component
@Scope("prototype")
@RequiredArgsConstructor
public class RequestOperator {

    @Autowired
    private final ApplicationEventPublisher publisher;
    protected List<BotApiMethod<Message>> messages = new ArrayList<>();


    public void addMessage(BotApiMethod<Message> messageBotApiMethod) {
        messages.add(messageBotApiMethod);
    }

    public void sendRequest() {
        for (var message : messages) {
            publish(message);
        }
    }


    protected void publish(BotApiMethod<Message> sendMessage) {
        var messageCreationEvent = MessageCreationEventFactory.create(sendMessage);
        publisher.publishEvent(messageCreationEvent);
    }
}
