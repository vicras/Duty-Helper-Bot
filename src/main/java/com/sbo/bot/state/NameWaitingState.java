package com.sbo.bot.state;

import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.handler.impl.FirstNameHandler;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Dmitars
 */
@Component
public class NameWaitingState extends State {
    private final FirstNameHandler firstNameHandler;
    private final SimpleRequestOperator requestOperator;

    public NameWaitingState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher, PersonService personService, FirstNameHandler firstNameHandler, SimpleRequestOperator requestOperator) {
        super(personProvider, publisher, personService);
        this.firstNameHandler = firstNameHandler;
        this.requestOperator = requestOperator;
    }

    @Override
    protected List<AbstractBaseHandler> getAvailableHandlers() {
        return List.of(firstNameHandler);
    }

    @Override
    protected RequestOperator getRequestOperator() {
        return requestOperator.builder()
                .line("Enter your first name. ")
                .line("Use only cyrillic and latin letters, numbers, _ , -.")
                .build();
    }
}
