package com.sbo.ability.commands;

import com.sbo.ability.State;
import com.sbo.ability.states.StartState;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Predicate;

import static com.sbo.ability.State.IDENTITY_STATE;
import static org.telegram.abilitybots.api.objects.Flag.NONE;

/**
 * @author viktar hraskou
 */
@Getter
public enum BaseCommands implements AbilityCommand {

    HELP(IDENTITY_STATE.getClass(), "Help message reproduce", NONE),
    START(StartState.class, "Start using bot with this command");

    private final Predicate<Update>[] flags;
    private final Class<? extends State> nextState;
    private String info;

    @SafeVarargs
    <T extends State> BaseCommands(Class<? extends State> nextState, Predicate<Update>... flag) {
        this.nextState = nextState;
        this.flags = flag;
        this.info = "";
    }

    @SafeVarargs
    <T extends State> BaseCommands(Class<? extends State> nextState, String info, Predicate<Update>... flag) {
        this(nextState, flag);
        this.info = info;
    }

}
