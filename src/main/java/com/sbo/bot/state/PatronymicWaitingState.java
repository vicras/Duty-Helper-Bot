package com.sbo.bot.state;

import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.handler.impl.PatronymicHandler;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Dmitars
 */
@Component
public class PatronymicWaitingState extends State{
    private final PatronymicHandler patronymicHandler;
    private final SimpleRequestOperator requestOperator;

    public PatronymicWaitingState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher, PersonService personService, PatronymicHandler patronymicHandler, SimpleRequestOperator requestOperator) {
        super(personProvider, publisher, personService);
        this.patronymicHandler = patronymicHandler;
        this.requestOperator = requestOperator;
    }

    @Override
    protected List<AbstractBaseHandler> getAvailableHandlers() {
        return List.of(patronymicHandler);
    }

    @Override
    protected RequestOperator getRequestOperator() {
        return requestOperator.builder()
                .line("Enter your patronymic.")
                .line("Use only cyrillic and latin letters, numbers, _ , -.")
                .build();
    }
}
