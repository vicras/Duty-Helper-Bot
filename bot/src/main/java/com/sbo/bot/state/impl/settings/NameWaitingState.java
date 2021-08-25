package com.sbo.bot.state.impl.settings;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.handler.BaseHandler;
import com.sbo.bot.handler.impl.settings.BackSettingsHandler;
import com.sbo.bot.handler.impl.settings.FirstNameHandler;
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

/**
 * @author Dmitars
 */
@Slf4j
@Component
public class NameWaitingState extends State {

    private final FirstNameHandler firstNameHandler;
    private final BackSettingsHandler backSettingsHandler;

    public NameWaitingState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher,
                            PersonService personService, FirstNameHandler firstNameHandler, BackSettingsHandler backSettingsHandler) {
        super(personProvider, publisher, personService);
        this.firstNameHandler = firstNameHandler;
        this.backSettingsHandler = backSettingsHandler;
    }

    @Override
    protected List<BaseHandler> getAvailableHandlers() {
        return List.of(firstNameHandler, backSettingsHandler);
    }

    @Override
    protected RequestOperator getRequestOperator(Update update) {
        SendMessage message = InlineMessageBuilder.builder(personProvider.getCurrentPerson())
                .header("Send me your new name")
                .row()
                .button("Back", BACK)
                .build();

        return new RequestOperator(publisher)
                .addMessage(message, update);
    }
}
