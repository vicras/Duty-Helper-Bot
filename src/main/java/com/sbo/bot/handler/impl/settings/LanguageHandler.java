package com.sbo.bot.handler.impl.settings;

import com.sbo.bot.annotation.BotCommand;
import com.sbo.bot.handler.CommandBaseHandler;
import com.sbo.bot.handler.impl.enums.ButtonCommands;
import com.sbo.bot.state.State;
import com.sbo.bot.state.impl.settings.SettingState;
import com.sbo.entity.enums.Language;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import com.sbo.service.impl.AuthorizationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.sbo.bot.handler.impl.enums.ButtonCommands.LANGUAGE_EN;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.LANGUAGE_RU;
import static com.sbo.entity.enums.Language.ENGLISH;
import static com.sbo.entity.enums.Language.RUSSIAN;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
@BotCommand
public class LanguageHandler extends CommandBaseHandler {

    private final PersonService personService;

    public LanguageHandler(AuthorizationServiceImpl authorizationService, ApplicationEventPublisher publisher,
                           CurrentPersonProvider personProvider, PersonService personService) {
        super(authorizationService, publisher, personProvider);
        this.personService = personService;
    }

    @Override
    protected void handleMessage(Update message) {
        var command = extractCommand(message);
        Language language = ENGLISH;
        if (LANGUAGE_RU.equals(command)) {
            language = RUSSIAN;
        }
        personService.updatePersonLanguage(personProvider.getCurrentPersonId(), language);
    }

    @Override
    protected List<ButtonCommands> getCommandQualifiers() {
        return List.of(LANGUAGE_EN, LANGUAGE_RU);
    }

    @Override
    public Class<? extends State> getNextState() {
        return SettingState.class;
    }
}
