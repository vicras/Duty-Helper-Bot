package com.sbo.bot.handler.impl;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.handler.CommandBaseHandler;
import com.sbo.bot.handler.impl.enums.ButtonCommands;
import com.sbo.bot.security.AuthorizationService;
import com.sbo.bot.state.State;
import com.sbo.entity.enums.Language;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.context.ApplicationEventPublisher;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.sbo.bot.handler.impl.enums.ButtonCommands.LANGUAGE_EN;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.LANGUAGE_RU;
import static com.sbo.entity.enums.Language.ENGLISH;
import static com.sbo.entity.enums.Language.RUSSIAN;

/**
 * @author viktar hraskou
 */
public class LanguageHandler extends CommandBaseHandler {

    private final PersonService personService;

    public LanguageHandler(AuthorizationService authorizationService, ApplicationEventPublisher publisher, CurrentPersonProvider personProvider, PersonService personService) {
        super(authorizationService, publisher, personProvider);
        this.personService = personService;
    }

    @Override
    protected void handleMessage(Update message) {
        var command = extractCommand(message);
        Language language = ENGLISH;
        if(LANGUAGE_RU.equals(command)){
            language = RUSSIAN;
        }
        personService.updateLanguage(personProvider.getCurrentPersonId(), language);
        publishOkMessage(language);
    }

    @Override
    protected List<ButtonCommands> getCommandQualifiers() {
        return List.of(LANGUAGE_EN, LANGUAGE_RU);
    }

    private void publishOkMessage(Language language) {
        SendMessage message = InlineMessageBuilder.builder(personProvider.getCurrentPersonId())
                .header("Language _%s_ set successfully:)", language)
                .build();
        publish(message);
    }

    @Override
    public State getNextState() {
        // TODO return Settings state
        throw new NotYetImplementedException();
    }
}
