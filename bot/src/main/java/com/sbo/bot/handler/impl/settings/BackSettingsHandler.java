package com.sbo.bot.handler.impl.settings;

import com.sbo.bot.annotation.BotCommand;
import com.sbo.bot.handler.CommandBaseHandler;
import com.sbo.bot.handler.impl.enums.ButtonCommands;
import com.sbo.bot.state.State;
import com.sbo.bot.state.impl.settings.SettingState;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.impl.AuthorizationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.sbo.bot.handler.impl.enums.ButtonCommands.BACK;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
@BotCommand
public class BackSettingsHandler extends CommandBaseHandler {

    public BackSettingsHandler(AuthorizationServiceImpl authorizationService, ApplicationEventPublisher publisher,
                               CurrentPersonProvider personProvider) {
        super(authorizationService, publisher, personProvider);
    }

    @Override
    protected void handleMessage(Update message) {
        // TODO rewrite SwitchBaseHandler and use it instead of that
    }

    @Override
    public Class<? extends State> getNextState() {
        return SettingState.class;
    }

    @Override
    protected List<ButtonCommands> getCommandQualifiers() {
        return List.of(BACK);
    }
}
