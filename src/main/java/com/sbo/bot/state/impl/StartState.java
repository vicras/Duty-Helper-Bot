package com.sbo.bot.state.impl;

import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.handler.impl.StartHandler;
import com.sbo.bot.handler.impl.settings.SettingsHandler;
import com.sbo.bot.state.RequestOperator;
import com.sbo.bot.state.State;
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

    private final SettingsHandler settingsHandler;
    private final StartHandler startHandler;

    public StartState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher, PersonService personService, SettingsHandler settingsHandler, StartHandler startHandler) {
        super(personProvider, publisher, personService);
        this.settingsHandler = settingsHandler;
        this.startHandler = startHandler;
    }

    @Override
    protected List<AbstractBaseHandler> getAvailableHandlers() {
        return List.of(settingsHandler, startHandler);
    }

    @Override
    protected RequestOperator getRequestOperator(Update update) {
        return null;
    }
}
