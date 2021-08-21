package com.sbo.bot.orchestrator;

import com.sbo.bot.state.State;
import com.sbo.provider.CurrentPersonProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Component
@Slf4j
@Transactional
@RequiredArgsConstructor
public class HandlerOrchestrator {
    private final CurrentPersonProvider personProvider;
    private final List<State> states;

    public void operate(Update update) {
        var message = update.getMessage();
        try {
            var state = getCurrentPersonState();
            log.info("Current state={} and command={}", state.getClass().getSimpleName(), getStringCommand(update));
            state.process(update, this::getState);

        } catch (UnsupportedOperationException e) {
            log.error("Command: {} is unsupported", message.getText());
        }
    }

    private String getStringCommand(Update update) {
        return update.hasMessage() ? update.getMessage().getText() : update.getCallbackQuery().getData();
    }

    private State getCurrentPersonState() {
        String personStateName = personProvider.getCurrentPerson().getState();
        return getState(personStateName);
    }

    private State getState(String state) {
        return states.stream()
                .filter(isStateWithName(state))
                .findAny()
                .orElseThrow(criticalException(state));
    }

    private Predicate<State> isStateWithName(String stateName) {
        return state -> state.getClass().toString().equals(stateName);
    }

    private Supplier<RuntimeException> criticalException(String stateName) {
        return () -> {
            String message = "Internal error: state name " + stateName + " not found";
            log.error(message);
            return new RuntimeException(message);
        };
    }
}
