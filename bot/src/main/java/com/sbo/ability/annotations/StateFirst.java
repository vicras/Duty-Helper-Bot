package com.sbo.ability.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author viktar hraskou
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface StateFirst {
    //work only with SendMessage return type
    boolean tryEdit() default true;
}
