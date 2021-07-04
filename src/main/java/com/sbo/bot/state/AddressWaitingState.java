package com.sbo.bot.state;

import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.handler.impl.HomeAddressHandler;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Dmitars
 */
@Component
public class AddressWaitingState extends State{
    private final SimpleRequestOperator requestOperator;
    private final HomeAddressHandler homeAddressHandler;

    public AddressWaitingState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher, PersonService personService, SimpleRequestOperator requestOperator, HomeAddressHandler homeAddressHandler) {
        super(personProvider, publisher, personService);
        this.requestOperator = requestOperator;
        this.homeAddressHandler = homeAddressHandler;
    }

    @Override
    protected List<AbstractBaseHandler> getAvailableHandlers() {
        return List.of(homeAddressHandler);
    }

    @Override
    protected RequestOperator getRequestOperator() {
        return requestOperator.builder()
                .line("Enter your address")
                .build();
    }
}
