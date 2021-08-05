package com.sbo.bot.state.impl.management;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.handler.BaseHandler;
import com.sbo.bot.state.RequestOperator;
import com.sbo.bot.state.State;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.sbo.bot.handler.impl.enums.ButtonCommands.ALL_PERSON;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.BLOCKED_PERSONS;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.HOME;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.NEW_PERSON;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.SELF_DESTRUCTION;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
public class ManagementState extends State {

    public ManagementState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher,
                           PersonService personService) {
        super(personProvider, publisher, personService);
    }

    @Override
    protected List<BaseHandler> getAvailableHandlers() {
        throw new NotYetImplementedException();
    }

    @Override
    protected RequestOperator getRequestOperator(Update update) {
        var mess = getStatusMessage(update);

        return new RequestOperator(publisher)
                .addMessage(mess, update);
    }

    private SendMessage getStatusMessage(Update update) {
        return InlineMessageBuilder.builder(personProvider.getCurrentPerson())
                .header("Person management page")
                .row()
                .button("All persons", ALL_PERSON)
                .row()
                .button("Add new person", NEW_PERSON)
                .row()
                .button("Blocked persons", BLOCKED_PERSONS)
                .button("Self destruction", SELF_DESTRUCTION)
                .row()
                .button("Home", HOME)
                .build();
    }
}
