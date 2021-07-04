package com.sbo.bot.state;

import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.handler.impl.TelephoneHandler;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Dmitars
 */
@Component
public class PhoneWaitingState extends State{
    private final TelephoneHandler telephoneHandler;
    private final SimpleRequestOperator requestOperator;


    public PhoneWaitingState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher, PersonService personService, TelephoneHandler telephoneHandler, SimpleRequestOperator requestOperator) {
        super(personProvider, publisher, personService);
        this.telephoneHandler = telephoneHandler;
        this.requestOperator = requestOperator;
    }

    @Override
    protected List<AbstractBaseHandler> getAvailableHandlers() {
        return List.of(telephoneHandler);
    }

    @Override
    protected RequestOperator getRequestOperator() {
        return requestOperator.builder()
                .line("Enter your phone in one of formats:")
                .line("375291234567")
                .line("+375291234567")
                .line("+375 (29) 123-45-67")
                .build();
    }
}
