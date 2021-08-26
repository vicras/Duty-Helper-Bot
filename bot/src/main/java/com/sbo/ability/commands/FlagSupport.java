package com.sbo.ability.commands;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Predicate;

import static java.util.Objects.nonNull;

/**
 * @author viktar hraskou
 */
public enum FlagSupport implements Predicate<Update> {
    ;
    private final Predicate<Update> predicate;

    FlagSupport(Predicate<Update> predicate) {
        this.predicate = predicate;
    }

    public boolean test(Update update) {
        return nonNull(update) && predicate.test(update);
    }


}
