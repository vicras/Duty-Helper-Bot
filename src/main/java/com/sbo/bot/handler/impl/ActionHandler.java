package com.sbo.bot.handler.impl;

import com.sbo.bot.handler.CommandBaseHandler;
import com.sbo.bot.handler.impl.enums.ButtonCommands;
import com.sbo.bot.security.AuthorizationService;
import com.sbo.bot.state.State;
import com.sbo.provider.CurrentPersonProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author Dmitars
 */
@Slf4j
@Component
public class ActionHandler extends CommandBaseHandler {

    public ActionHandler(AuthorizationService authorizationService, ApplicationEventPublisher publisher, CurrentPersonProvider personProvider) {
        super(authorizationService, publisher, personProvider);
    }

    @Override
    protected List<ButtonCommands> getCommandQualifiers() {
        return null;
    }

    @Override
    public Class<? extends State> getNextState() {
        return null;
    }

    @Override
    public void handleMessage(Update update) {

    }

}
