package com.sbo.ability.commands;

import com.sbo.ability.State;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Predicate;

/**
 * @author viktar hraskou
 */
public enum StateCommands {
    ;
    private final Predicate<Update>[] flags;
    private final Class<? extends State> nextState;
    private final Class<? extends State> flagState;
    private String info;

    @SafeVarargs
    StateCommands(Class<? extends State> nextState, Class<? extends State> currentState, Predicate<Update>... flag) {
        this.nextState = nextState;
        this.flagState = currentState;
        this.flags = flag;
        this.info = "";
    }

    @SafeVarargs
    StateCommands(Class<? extends State> nextState,Class<? extends State> currentState, String info, Predicate<Update>... flag) {
        this(nextState, currentState, flag);
        this.info = info;
    }

}
