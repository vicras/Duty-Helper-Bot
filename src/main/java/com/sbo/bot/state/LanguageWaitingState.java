package com.sbo.bot.state;

import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.handler.impl.LanguageHandler;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Dmitars
 */
@Component
public class LanguageWaitingState extends State{
    private final LanguageHandler languageHandler;

    public LanguageWaitingState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher, PersonService personService, LanguageHandler languageHandler) {
        super(personProvider, publisher, personService);
        this.languageHandler = languageHandler;
    }

    @Override
    protected List<AbstractBaseHandler> getAvailableHandlers() {
        return List.of(languageHandler);
    }

    @Override
    protected RequestOperator getRequestOperator() {
        //TODO: request with buttons
        return null;
    }
}
