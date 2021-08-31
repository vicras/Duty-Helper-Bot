package com.sbo.ability.commands;

import com.sbo.ability.State;
import com.sbo.ability.states.SettingState;
import com.sbo.ability.states.StartState;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Predicate;

import static org.telegram.abilitybots.api.objects.Flag.NONE;

/**
 * @author viktar hraskou
 */
@Getter
public enum StateCommands implements AbilityStateCommand {
    SETTINGS(SettingState.class, StartState.class, "Help message reproduce", NONE),
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
    StateCommands(Class<? extends State> nextState, Class<? extends State> currentState, String info, Predicate<Update>... flag) {
        this(nextState, currentState, flag);
        this.info = info;
    }

    public String getCommand() {
        return "/" + name().toLowerCase();
    }

    public String getCommand(String... args) {
        return "/" + name().toLowerCase() + " " + String.join(" ", args);
    }

}
