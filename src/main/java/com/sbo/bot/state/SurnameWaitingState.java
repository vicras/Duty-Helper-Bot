package com.sbo.bot.state;

import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.handler.impl.LastNameHandler;
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
public class SurnameWaitingState extends State {
    private final LastNameHandler lastNameHandler;
    private final RequestOperator requestOperator;

    public SurnameWaitingState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher, PersonService personService, LastNameHandler lastNameHandler, RequestOperator requestOperator) {
        super(personProvider, publisher, personService);
        this.lastNameHandler = lastNameHandler;
        this.requestOperator = requestOperator;
    }

    @Override
    protected List<AbstractBaseHandler> getAvailableHandlers() {
        return List.of(lastNameHandler);
    }

    @Override
    protected RequestOperator getRequestOperator() {
        SendMessage sendMessage = SendMessage.builder()
                .text("Enter your first name.")
                .text("Use only cyrillic and latin letters, numbers, _ , -.")
                .build();
        requestOperator.addMessage(sendMessage);
        return requestOperator;
    }
}
