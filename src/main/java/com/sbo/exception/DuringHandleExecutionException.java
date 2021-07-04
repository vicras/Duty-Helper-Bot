package com.sbo.exception;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.List;

/**
 * @author viktar hraskou
 */
public class DuringHandleExecutionException extends RuntimeException {

    @Getter
    private final List<BotApiMethod<?>> methods;

    public DuringHandleExecutionException(List<BotApiMethod<?>> methods, String message) {
        super(message);
        this.methods = methods;
    }
}
