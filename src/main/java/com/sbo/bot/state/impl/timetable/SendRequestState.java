package com.sbo.bot.state.impl.timetable;

import com.sbo.bot.handler.BaseHandler;
import com.sbo.bot.state.RequestOperator;
import com.sbo.bot.state.State;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.context.ApplicationEventPublisher;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author viktar hraskou
 */
public class SendRequestState extends State {

    public SendRequestState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher, PersonService personService) {
        super(personProvider, publisher, personService);
    }

    @Override
    protected List<BaseHandler> getAvailableHandlers() {
        throw new NotYetImplementedException();
    }

    @Override
    protected RequestOperator getRequestOperator(Update update) {
        throw new NotYetImplementedException();
    }
}
