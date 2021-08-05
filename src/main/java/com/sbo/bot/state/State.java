package com.sbo.bot.state;

import com.sbo.bot.events.ApiMethodsCreationEvent;
import com.sbo.bot.handler.BaseHandler;
import com.sbo.exception.DuringHandleExecutionException;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.function.Function;

import static java.util.Objects.nonNull;

/**
 * @author Dmitars
 */
@Slf4j
@Component
@RequiredArgsConstructor
public abstract class State {
    protected final CurrentPersonProvider personProvider;
    protected final ApplicationEventPublisher publisher;
    protected final PersonService personService;

    public void setNextState(State state, Update update) {
        personService.updatePersonState(personProvider.getCurrentPersonId(), state);
        state.sendRequest(update);
    }

    public void process(Update update, Function<String, State> stateByName) {

        getAvailableHandlers().stream()
                .filter(handler -> handler.canProcessMessage(update))
                .findFirst()
                .ifPresentOrElse(
                        handler -> processUpdateWithHandler(handler, update, stateByName),
                        this::updateCannotBeProcessed
                );

    }

    private void processUpdateWithHandler(BaseHandler handler, Update update, Function<String, State> stateByName) {
        log.info("Find Handler: {}", handler);

        operateHandleExecution(handler, update, stateByName);
    }

    private void operateHandleExecution(BaseHandler handler, Update update, Function<String, State> stateByName) {
        try {
            handler.authorizeAndHandle(update);
            setNextState(stateByName.apply(handler.getNextState().toString()), update);
        } catch (DuringHandleExecutionException ex) {
            log.info("Can't handle with reason {}", ex.getMessage());
            ex.getMethods().forEach(this::publish);
        }
    }

    private void updateCannotBeProcessed() {
        // TODO think about how to user will know about it
        log.error("Can't find suitable handler for this command at this moment!!!");
//        throw new RuntimeException("This format of message cannot be processed at that time");
    }

    protected final void publish(BotApiMethod<?> message) {
        this.publisher.publishEvent(ApiMethodsCreationEvent.of(message));
    }

    protected abstract List<BaseHandler> getAvailableHandlers();

    protected abstract RequestOperator getRequestOperator(Update update);

    protected void sendRequest(Update update) {
        if (nonNull(getRequestOperator(update))) {
            getRequestOperator(update).sendRequest();
        }
    }
}
