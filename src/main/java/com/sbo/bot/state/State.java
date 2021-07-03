package com.sbo.bot.state;

import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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

        handler.authorizeAndHandle(update);
        next(handler.getNextState());
    }

    private void updateCannotBeProcessed() {
        // TODO think about how to user will know about it
        log.info("Can't find suitable handler for this command at this moment!!!");

        throw new RuntimeException("This format of message cannot be processed at that time");
    }

    protected abstract List<AbstractBaseHandler> getAvailableHandlers();

    protected abstract RequestOperator getRequestOperator();

    protected void sendRequest() {
        getRequestOperator().sendRequest();
    }
}
