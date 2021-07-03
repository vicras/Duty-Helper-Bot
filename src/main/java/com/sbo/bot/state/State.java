package com.sbo.bot.state;

import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.provider.CurrentPersonProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author Dmitars
 */
@Component
@RequiredArgsConstructor
public abstract class State {
    protected final CurrentPersonProvider personProvider;

    public void next(State state) {
        personProvider.getPersonService().updateState(personProvider.getCurrentPerson(), state);
        state.sendRequest();
    }

    public void process(Update update) {
        boolean canBeProcessed = false;
        for (var handler : getAvailableHandlers()) {
            if (handler.canProcessMessage(update)) {
                handler.handleMessage(update.getMessage());
                next(handler.getNextState());
                canBeProcessed = true;
                break;
            }
        }
        if (!canBeProcessed)
            throw new RuntimeException("This format of message cannot be processed at that time");
    }

    protected abstract List<AbstractBaseHandler> getAvailableHandlers();

    protected abstract RequestOperator getRequestOperator();

    protected void sendRequest() {
        getRequestOperator().sendRequest();
    }
}
