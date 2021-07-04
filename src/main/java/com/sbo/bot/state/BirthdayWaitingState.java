package com.sbo.bot.state;

import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.handler.impl.BirthdayHandler;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Dmitars
 */
@Component
public class BirthdayWaitingState extends State{
    private final SimpleRequestOperator requestOperator;
    private final BirthdayHandler birthdayHandler;


    public BirthdayWaitingState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher, PersonService personService, SimpleRequestOperator requestOperator, BirthdayHandler birthdayHandler) {
        super(personProvider, publisher, personService);
        this.requestOperator = requestOperator;
        this.birthdayHandler = birthdayHandler;
    }

    @Override
    protected List<AbstractBaseHandler> getAvailableHandlers() {
        return List.of(birthdayHandler);
    }

    @Override
    protected RequestOperator getRequestOperator() {
        return requestOperator.builder()
                .line("Enter your birthday date (dd.mm.yyyy | yyyymmdd | yyyy.mm.dd | yyyy/mm/dd | dd/mm/yyyy):")
                .build();
    }
}
