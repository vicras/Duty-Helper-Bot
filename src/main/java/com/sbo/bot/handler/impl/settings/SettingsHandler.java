package com.sbo.bot.handler.impl.settings;

import com.sbo.bot.annotation.BotCommand;
import com.sbo.bot.handler.CommandBaseHandler;
import com.sbo.bot.handler.impl.enums.ButtonCommands;
import com.sbo.bot.security.AuthorizationService;
import com.sbo.bot.state.State;
import com.sbo.bot.state.impl.settings.SettingState;
import com.sbo.provider.CurrentPersonProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.sbo.bot.handler.impl.enums.ButtonCommands.SETTINGS;

/**
 * @author viktar hraskou
 */
// TODO delete this handler and use SwitchBaseHandler
@Slf4j
@Component
@BotCommand
public class SettingsHandler extends CommandBaseHandler {

    public SettingsHandler(AuthorizationService authorizationService, ApplicationEventPublisher publisher,
                           CurrentPersonProvider personProvider) {
        super(authorizationService, publisher, personProvider);
    }

    @Override
    protected void handleMessage(Update message) {
    }

    @Override
    public Class<? extends State> getNextState() {
        return SettingState.class;
    }

    @Override
    protected List<ButtonCommands> getCommandQualifiers() {
        return List.of(SETTINGS);
    }

}
