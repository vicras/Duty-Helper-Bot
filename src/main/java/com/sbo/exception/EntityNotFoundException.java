package com.sbo.exception;

import static java.lang.String.format;

/**
 * @author viktar hraskou
 */
public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(Class<?> entity, Long id) {
        super(format("Entity %s with id='%d' not found", entity, id));
    }
}
