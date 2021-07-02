package com.sbo.bot.state;

import com.sbo.bot.builder.MessageBuilder;
import com.sbo.provider.CurrentPersonProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * @author Dmitars
 */
@Component
public abstract class PreparedState extends State {

    public PreparedState(ApplicationEventPublisher publisher, CurrentPersonProvider personProvider) {
        super(publisher, personProvider);
    }

    protected abstract String getRequest();

    protected void sendRequest() {
        SendMessage sendMessage = MessageBuilder.builder(personProvider.getCurrentPerson())
                .line(getRequest())
                .build();
        publish(sendMessage);
    }
}


