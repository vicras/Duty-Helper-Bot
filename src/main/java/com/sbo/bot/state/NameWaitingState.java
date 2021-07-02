package com.sbo.bot.state;

import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.provider.CurrentPersonProvider;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

/**
 * @author Dmitars
 */
public class NameWaitingState extends PreparedState{

    public NameWaitingState(ApplicationEventPublisher publisher, CurrentPersonProvider personProvider) {
        super(publisher, personProvider);
    }

    @Override
    protected String getRequest() {
        return "Please, enter your name:";
    }


    @Override
    protected List<AbstractBaseHandler> getAvailableHandlers() {
        return null;
    }
}
