package com.sbo.bot.state;

import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.handler.impl.EmailHandler;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Dmitars
 */
@Component
public class EmailWaitingState extends State{
    private final SimpleRequestOperator requestOperator;
    private final EmailHandler emailHandler;

    public EmailWaitingState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher, PersonService personService, SimpleRequestOperator requestOperator, EmailHandler emailHandler) {
        super(personProvider, publisher, personService);
        this.requestOperator = requestOperator;
        this.emailHandler = emailHandler;
    }

    @Override
    protected List<AbstractBaseHandler> getAvailableHandlers() {
        return List.of(emailHandler);
    }

    @Override
    protected RequestOperator getRequestOperator() {
        return requestOperator.builder()
                .line("Enter your email:")
                .build();
    }
}
