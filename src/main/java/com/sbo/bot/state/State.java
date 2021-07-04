package com.sbo.bot.state;

import com.sbo.bot.events.ApiMethodsCreationEvent;
import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.exception.DuringHandleExecutionException;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author Dmitars
 */
@Slf4j
@Component
@RequiredArgsConstructor
public abstract class State {
    protected final CurrentPersonProvider personProvider;
    protected final ApplicationEventPublisher publisher;
    private final PersonService personService;

    public void next(State state) {
        personService.updateState(personProvider.getCurrentPerson(), state);
        state.sendRequest();
    }

    public void process(Update update) {

        getAvailableHandlers().stream()
                .filter(handler -> handler.canProcessMessage(update))
                .findFirst()
                .ifPresentOrElse(
                        handler -> processUpdateWithHandler(handler, update),
                        this::updateCannotBeProcessed
                );

    }

    private void processUpdateWithHandler(AbstractBaseHandler handler, Update update) {
        log.info("Find Handler: {}", handler);

        operateHandleExecution(handler, update);
        next(handler.getNextState());
    }

    private void operateHandleExecution(AbstractBaseHandler handler, Update update) {
        try {
            handler.authorizeAndHandle(update);
        } catch (DuringHandleExecutionException ex) {
            log.info("Can't handle with reason {}", update.getMessage());
            ex.getMethods().forEach(ApiMethodsCreationEvent::of);
        }
    }

    private void updateCannotBeProcessed() {
        // TODO think about how to user will know about it
        log.info("Can't find suitable handler for this command at this moment!!!");

        throw new RuntimeException("This format of message cannot be processed at that time");
    }

    protected final void publish(SendMessage message) {
        this.publisher.publishEvent(ApiMethodsCreationEvent.of(message));
    }

    protected abstract List<AbstractBaseHandler> getAvailableHandlers();

    protected abstract RequestOperator getRequestOperator();

    protected void sendRequest() {
        getRequestOperator().sendRequest();
    }
}
