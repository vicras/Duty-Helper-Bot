package com.sbo.bot.state.impl.settings;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.handler.BaseHandler;
import com.sbo.bot.handler.impl.settings.BackSettingsHandler;
import com.sbo.bot.handler.impl.settings.LanguageHandler;
import com.sbo.bot.state.RequestOperator;
import com.sbo.bot.state.State;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.sbo.bot.handler.impl.enums.ButtonCommands.BACK;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.LANGUAGE_EN;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.LANGUAGE_RU;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
public class LanguageWaitingState extends State {

    private final LanguageHandler languageHandler;
    private final BackSettingsHandler backSettingsHandler;

    public LanguageWaitingState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher, PersonService personService, LanguageHandler languageHandler, BackSettingsHandler backSettingsHandler) {
        super(personProvider, publisher, personService);
        this.languageHandler = languageHandler;
        this.backSettingsHandler = backSettingsHandler;
    }

    @Override
    protected List<BaseHandler> getAvailableHandlers() {
        return List.of(languageHandler, backSettingsHandler);
    }

    @Override
    protected RequestOperator getRequestOperator(Update update) {
        SendMessage message = InlineMessageBuilder.builder(personProvider.getCurrentPerson())
                .header("Choose language:")
                .row()
                .button("Russian", LANGUAGE_RU)
                .button("English", LANGUAGE_EN)
                .row()
                .button("Back", BACK)
                .build();

        return new RequestOperator(publisher)
                .addMessage(message, update);
    }
}
