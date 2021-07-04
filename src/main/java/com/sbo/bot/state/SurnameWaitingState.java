package com.sbo.bot.state;

import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.handler.impl.LastNameHandler;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Dmitars
 */
@Component
public class SurnameWaitingState extends State {
    private final LastNameHandler lastNameHandler;
    private final SimpleRequestOperator requestOperator;

    public SurnameWaitingState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher, PersonService personService, LastNameHandler lastNameHandler, SimpleRequestOperator requestOperator) {
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
        return requestOperator.builder()
                .line("Enter your last name.")
                .line("Use only cyrillic and latin letters, numbers, _ , -.")
                .build();
    }
}
