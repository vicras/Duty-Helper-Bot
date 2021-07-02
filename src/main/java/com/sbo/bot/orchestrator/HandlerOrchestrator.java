package com.sbo.bot.orchestrator;

import com.sbo.bot.state.State;
import com.sbo.provider.CurrentPersonProvider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.function.Supplier;

/**
 * Chooses suitable inheritor of AbstractBaseHandler to handle the input
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class HandlerOrchestrator {
    private final CurrentPersonProvider personProvider;
    private final List<State> states;

    public void operate(Update update) {
        var message = update.getMessage();
        try {
            var state = getState();
            log.debug("Found state {} for command {}", state.getClass().getSimpleName(), message.getText());
            state.process(update);
        } catch (UnsupportedOperationException e) {
            log.error("Command: {} is unsupported", message.getText());
        }
    }

    @SneakyThrows
    public State getState(){
        String stateName = personProvider.getCurrentPerson().getState();
        return states.stream()
                .filter(state -> state.getClass().toString().equals(stateName))
                .findAny()
                .orElseThrow((Supplier<Throwable>) () -> new RuntimeException("Internal error: state name "+stateName+" not found"));
    }
}
