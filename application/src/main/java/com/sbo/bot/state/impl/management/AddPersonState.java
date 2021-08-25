package com.sbo.bot.state.impl.management;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.handler.BaseHandler;
import com.sbo.bot.handler.SwitchHandler;
import com.sbo.bot.handler.impl.management.NewPersonHandler;
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
public class AddPersonState extends State {

    private final NewPersonHandler newPersonHandler;

    public AddPersonState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher,
                          PersonService personService, NewPersonHandler newPersonHandler) {
        super(personProvider, publisher, personService);
        this.newPersonHandler = newPersonHandler;
    }

    @Override
    protected List<BaseHandler> getAvailableHandlers() {
        return List.of(
                newPersonHandler,
                SwitchHandler.of(ManagementState.class, BACK)
        );
    }

    @Override
    protected RequestOperator getRequestOperator(Update update) {
        var mess = getStatusMessage(update);

        return new RequestOperator(publisher)
                .addMessage(mess, update);
    }

    private SendMessage getStatusMessage(Update update) {
        return InlineMessageBuilder.builder(personProvider.getCurrentPerson())
                .header("To add new person")
                .line("Do one:")
                .line("- Forward me his message")
                .line("- Send me his telegram ID")

                .row()
                .button("Back", BACK)
                .build();
    }
}
