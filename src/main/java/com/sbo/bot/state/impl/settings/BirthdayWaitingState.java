package com.sbo.bot.state.impl.settings;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.handler.impl.settings.BackSettingsHandler;
import com.sbo.bot.handler.impl.settings.BirthdayHandler;
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
 * @author viktar hraskou
 */
@Slf4j
@Component
public class BirthdayWaitingState extends State {

    private final BirthdayHandler birthdayHandler;
    private final BackSettingsHandler backSettingsHandler;

    public BirthdayWaitingState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher, PersonService personService, BirthdayHandler birthdayHandler, BackSettingsHandler backSettingsHandler) {
        super(personProvider, publisher, personService);
        this.birthdayHandler = birthdayHandler;
        this.backSettingsHandler = backSettingsHandler;
    }

    @Override
    protected List<AbstractBaseHandler> getAvailableHandlers() {
        return List.of(birthdayHandler, backSettingsHandler);
    }

    @Override
    protected RequestOperator getRequestOperator(Update update) {
        SendMessage message = InlineMessageBuilder.builder(personProvider.getCurrentPerson())
                .header("Send me your new birth day")
                .line("Use one of this formats:")
                .line("- 20001130")
                .line("- 30.11.2000")
                .line("- 2000.11.30")
                .line("- 2000/11/30")
                .line("- 30/11/2000")
                .row()
                .button("Back", BACK)
                .build();

        return new RequestOperator(publisher)
                .addMessage(message, update);
    }
}
