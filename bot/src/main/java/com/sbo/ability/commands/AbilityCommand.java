package com.sbo.ability.commands;

import com.sbo.ability.State;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Predicate;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.ADMIN;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

/**
 * @author viktar hraskou
 */
public interface AbilityCommand {
    Predicate<Update>[] getFlags();

    String name();

    Class<? extends State> getNextState();

    default String getInfo() {
        return "";
    }

    default int argsAmounts() {
        return 0;
    }

    default Ability.AbilityBuilder defaultUserAbilityWithoutActions() {
        return getAbilityBuilder()
                .privacy(PUBLIC);
    }

    default Ability.AbilityBuilder defaultAdminAbilityWithoutActions() {
        return getAbilityBuilder()
                .privacy(ADMIN);
    }

    private Ability.AbilityBuilder getAbilityBuilder() {
        return Ability
                .builder()
                .name(name().toLowerCase())
                .info(getInfo())
                .input(argsAmounts())
                .flag(getFlags())
                .locality(USER);

    }
}
