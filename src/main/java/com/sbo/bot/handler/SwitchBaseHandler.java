package com.sbo.bot.handler;

import com.sbo.bot.annotation.BotCommand;
import com.sbo.bot.handler.impl.enums.ButtonCommands;
import com.sbo.bot.security.AuthorizationService;
import com.sbo.bot.state.State;
import com.sbo.provider.CurrentPersonProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author viktar hraskou
 * Base class for handlers used to toggle state
 */
@Slf4j
@BotCommand()
public class SwitchBaseHandler extends CommandBaseHandler {

    private final CommandToState commandToState;

    public SwitchBaseHandler(AuthorizationService authorizationService, ApplicationEventPublisher publisher, CurrentPersonProvider personProvider, CommandToState commandToState) {
        super(authorizationService, publisher, personProvider);
        this.commandToState = commandToState;
    }

    @Override
    protected void handleMessage(Update message) {

    }

    @Override
    protected List<ButtonCommands> getCommandQualifiers() {
        return List.of(commandToState.qualifiers);
    }

    @Override
    public Class<? extends State> getNextState() {
        return commandToState.getNextState();
    }

    @Data
    @AllArgsConstructor
    static public class CommandToState {
        private final ButtonCommands qualifiers;
        private final Class<? extends State> nextState;
    }
}
