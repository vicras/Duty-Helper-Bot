package com.sbo.bot.state;

import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.handler.impl.ActionHandler;
import com.sbo.bot.handler.impl.HelpHandler;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Dmitars
 */
@Component
public class LoginState extends State {

    private final HelpHandler helpHandler;
    private final ActionHandler actionHandler;

    public LoginState(PersonService personService, CurrentPersonProvider personProvider, HelpHandler helpHandler, ActionHandler actionHandler) {
        super(personProvider, personService);
        this.helpHandler = helpHandler;
        this.actionHandler = actionHandler;
    }

    @Override
    protected List<AbstractBaseHandler> getAvailableHandlers() {
        return List.of(helpHandler, actionHandler);
    }

    @Override
    protected RequestOperator getRequestOperator() {
        return null;
    }
}
