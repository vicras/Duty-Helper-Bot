package com.sbo.bot.state.impl.settings;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.builder.MessageBuilder;
import com.sbo.bot.handler.BaseHandler;
import com.sbo.bot.handler.impl.settings.BackSettingsHandler;
import com.sbo.bot.handler.impl.settings.TelephoneHandler;
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
public class TelephoneWaitingState extends State {

    private final TelephoneHandler telephoneHandler;
    private final BackSettingsHandler backSettingsHandler;

    public TelephoneWaitingState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher, PersonService personService, TelephoneHandler telephoneHandler, BackSettingsHandler backSettingsHandler) {
        super(personProvider, publisher, personService);
        this.telephoneHandler = telephoneHandler;
        this.backSettingsHandler = backSettingsHandler;
    }

    @Override
    protected List<BaseHandler> getAvailableHandlers() {
        return List.of(telephoneHandler, backSettingsHandler);
    }

    @Override
    protected RequestOperator getRequestOperator(Update update) {
        SendMessage message = InlineMessageBuilder.builder(personProvider.getCurrentPerson())
                .header("Send me your new telephone")
                .row()
                .button("Back", BACK)
                .build();

        SendMessage telButton = MessageBuilder.builder(personProvider.getCurrentPerson())
                .line("Supported formats:")
                .line("-  375297473331")
                .line("-  +375297473331")
                .line("-  +375 (29) 747-33-31")
                .line("Or use button in the bottom")
                .row()
                .telButton("Send Telegram account telephone")
                .build();

        return new RequestOperator(publisher)
                .addMessage(message, update)
                .addMessage(telButton, update);
    }
}
