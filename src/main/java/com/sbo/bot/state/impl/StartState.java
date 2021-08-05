package com.sbo.bot.state.impl;

import com.sbo.bot.handler.BaseHandler;
import com.sbo.bot.handler.SwitchHandler;
import com.sbo.bot.handler.impl.StartHandler;
import com.sbo.bot.handler.impl.enums.ButtonCommands;
import com.sbo.bot.state.RequestOperator;
import com.sbo.bot.state.State;
import com.sbo.bot.state.impl.settings.SettingState;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
public class StartState extends State {

    private final StartHandler startHandler;

    public StartState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher,
                      PersonService personService, StartHandler startHandler) {
        super(personProvider, publisher, personService);
        this.startHandler = startHandler;
    }

    @Override
    protected List<BaseHandler> getAvailableHandlers() {
        return List.of(
                SwitchHandler.of(SettingState.class, ButtonCommands.SETTINGS),
                startHandler
        );
    }

    @Override
    protected RequestOperator getRequestOperator(Update update) {
        return null;
    }
}
