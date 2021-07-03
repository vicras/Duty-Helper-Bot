package com.sbo.bot.orchestrator;

import com.sbo.bot.state.State;
import com.sbo.provider.CurrentPersonProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
            log.info("Current state={} and command={}", state.getClass().getSimpleName(), message.getText());
            state.process(update);
        } catch (UnsupportedOperationException e) {
            log.error("Command: {} is unsupported", message.getText());
        }
    }

    public State getState() {
        String personStateName = personProvider.getCurrentPerson().getState();
        return states.stream()
                .filter(isStateWithName(personStateName))
                .findAny()
                .orElseThrow(criticalException(personStateName));
    }

    private Predicate<State> isStateWithName(String stateName) {
        return state -> state.getClass().toString().equals(stateName);
    }

    private Supplier<RuntimeException> criticalException(String stateName) {
        String message = "Internal error: state name " + stateName + " not found";
        log.error(message);
        return () -> new RuntimeException(message);
    }
}
