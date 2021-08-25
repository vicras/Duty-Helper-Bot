package com.sbo.exception;

import static java.lang.String.format;

/**
 * @author viktar hraskou
 */
public class UserNameIsNullException extends RuntimeException {
    public UserNameIsNullException(Long telegramId) {
        super(format("Username for user with telegram id=%s is null", telegramId));
    }
}
