package com.sbo.bot.annotation;

import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.domain.postgres.entity.enums.PersonRole;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.sbo.domain.postgres.entity.enums.PersonRole.UNAUTHORIZED;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates that a component is a valid handler for bot command
 * <p>
 * // * Used on inheritors of {@link AbstractBaseHandler}
 * <p>
 * // * @see DebtHandler example of implementation
 */


@Retention(RUNTIME)
@Target(TYPE)
public @interface BotCommand {

    /**
     * Returns an array of user roles that have access to the handler
     * Default: {@link PersonRole#UNAUTHORIZED} - every user can call this handler
     *
     * @return an array of user roles that have access to the handler
     */
    PersonRole[] requiredRoles() default UNAUTHORIZED;
}
