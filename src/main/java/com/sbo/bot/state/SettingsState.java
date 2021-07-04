package com.sbo.bot.state;

import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

/**
 * @author Dmitars
 */
@Component
public class SettingsState extends State {
    private final RequestOperator requestOperator;

    public SettingsState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher, PersonService personService, RequestOperator requestOperator) {
        super(personProvider, publisher, personService);
        this.requestOperator = requestOperator;
    }


    @Override
    protected List<AbstractBaseHandler> getAvailableHandlers() {
        return List.of();
    }

    @Override
    protected RequestOperator getRequestOperator() {
        SendMessage sendMessage = SendMessage.builder()
                .text("Here you can set all your contact information. Until every info is filled in you cannot go to another functions ")
                .build();
        requestOperator.addMessage(sendMessage);
        return requestOperator;
    }
}
