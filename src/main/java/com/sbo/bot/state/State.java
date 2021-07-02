package com.sbo.bot.state;

import com.sbo.bot.events.SendMessageCreationEvent;
import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.provider.CurrentPersonProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author Dmitars
 */
@Component
@RequiredArgsConstructor
public abstract class State {
    protected final ApplicationEventPublisher publisher;
    protected final CurrentPersonProvider personProvider;

    public void next(State state) {
        if (state instanceof PreparedState)
            ((PreparedState) state).sendRequest();
        personProvider.getPersonService().updateState(personProvider.getCurrentPerson(), state);
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

    protected void publish(SendMessage sendMessage) {
        publisher.publishEvent(SendMessageCreationEvent.of(sendMessage));
    }
}
