package com.sbo.bot.handler.impl;

import com.sbo.bot.handler.CommandBaseHandler;
import com.sbo.bot.security.AuthorizationService;
import com.sbo.bot.state.State;
import com.sbo.provider.CurrentPersonProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Dmitars
 */
@Component
public class ActionHandler extends CommandBaseHandler {

    public ActionHandler(AuthorizationService authorizationService, ApplicationEventPublisher publisher, CurrentPersonProvider personProvider) {
        super(authorizationService, publisher, personProvider);
    }

    @Override
    protected String getCommandQualifier() {
        return "action";
    }

    @Override
    public State getNextState() {
        return null;
    }

    @Override
    public void handleMessage(Update update) {

    }

}
