package com.sbo.bot.state;

import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.provider.CurrentPersonProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

/**
 * @author Dmitars
 */
@Component
public class NameWaitingState extends State {
    @Autowired
    private RequestOperator requestOperator;


    public NameWaitingState(CurrentPersonProvider personProvider) {
        super(personProvider);
    }


    @Override
    protected List<AbstractBaseHandler> getAvailableHandlers() {
        return null;
    }

    @Override
    protected RequestOperator getRequestOperator() {
        SendMessage sendMessage = SendMessage.builder()
                .text("Please,enter your name:")
                .build();
        requestOperator.addMessage(sendMessage);
        return requestOperator;
    }
}
