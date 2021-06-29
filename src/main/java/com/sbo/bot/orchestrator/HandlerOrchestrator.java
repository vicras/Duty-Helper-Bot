package com.sbo.bot.orchestrator;

import com.sbo.bot.annotation.BotCommand;
import com.sbo.bot.handler.AbstractBaseHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

/**
 * Chooses suitable inheritor of AbstractBaseHandler to handle the input
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class HandlerOrchestrator {
    private final List<AbstractBaseHandler> handlers;
//    private final UserService userService;

    public void operate(int userId, String text) {
        try {
            AbstractBaseHandler handler = getHandler(text);
            log.debug("Found handler {} for command {}", handler.getClass().getSimpleName(), text);
//            handler.authorizeAndHandle(userService.getOrCreate(userId), text);
        } catch (UnsupportedOperationException e) {
            log.error("Command: {} is unsupported", text);
        }
    }

    /**
     * Selects handler which can handle received command
     *
     * @param text content of received message
     * @return handler suitable for command
     */
    protected AbstractBaseHandler getHandler(String text) {
        return handlers.stream()
                .filter(this::isBotAnnotationPresent)
                .filter(handler -> isCommandHandledByHandler(extractCommand(text), handler))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private boolean isCommandHandledByHandler(String command, AbstractBaseHandler h) {
        return Stream.of(h.getClass()
                .getAnnotation(BotCommand.class)
                .command()
        )
                .anyMatch(c -> c.equalsIgnoreCase(command));
    }

    private boolean isBotAnnotationPresent(AbstractBaseHandler h) {
        return h.getClass()
                .isAnnotationPresent(BotCommand.class);
    }

    private String extractCommand(String text) {
        return text.split(" ")[0];
    }
}
