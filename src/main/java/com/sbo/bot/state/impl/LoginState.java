package com.sbo.bot.state.impl;

import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.handler.impl.ActionHandler;
import com.sbo.bot.state.RequestOperator;
import com.sbo.bot.state.State;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import org.springframework.context.ApplicationEventPublisher;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author Dmitars
 */
public class LoginState extends State {

    private final ActionHandler actionHandler;

    public LoginState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher, PersonService personService, ActionHandler actionHandler) {
        super(personProvider, publisher, personService);
        this.actionHandler = actionHandler;
    }

    @Override
    protected List<AbstractBaseHandler> getAvailableHandlers() {
        return List.of(actionHandler);
    }

    @Override
    protected RequestOperator getRequestOperator(Update update) {
        return null;
    }
}
