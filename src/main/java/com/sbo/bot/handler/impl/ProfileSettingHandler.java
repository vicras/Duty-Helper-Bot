package com.sbo.bot.handler.impl;

import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.security.AuthorizationService;
import com.sbo.bot.state.SettingsState;
import com.sbo.bot.state.State;
import com.sbo.provider.CurrentPersonProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author Dmitars
 */
@Component
public abstract class ProfileSettingHandler extends AbstractBaseHandler {
    @Autowired
    private SettingsState state;

    public ProfileSettingHandler(AuthorizationService authorizationService, ApplicationEventPublisher publisher, CurrentPersonProvider personProvider) {
        super(authorizationService, publisher, personProvider);
    }

    @Override
    public State getNextState() {
        return state;
    }
}
