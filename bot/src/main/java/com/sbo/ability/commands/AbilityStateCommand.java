package com.sbo.ability.commands;

import com.sbo.ability.State;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.Ability;

import static com.sbo.ability.State.getStateChecker;
import static org.apache.commons.lang3.ArrayUtils.add;
import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.ADMIN;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

/**
 * @author viktar hraskou
 */
public interface AbilityStateCommand extends AbilityCommand {
    Class<? extends State> getFlagState();

    default Ability defaultUserAbilityWithoutActions(DBContext db) {
        return getAbilityBuilder(db)
                .privacy(PUBLIC)
                .build();
    }

    default Ability defaultAdminAbilityWithoutActions(DBContext db) {
        return getAbilityBuilder(db)
                .privacy(ADMIN)
                .build();
    }

    private Ability.AbilityBuilder getAbilityBuilder(DBContext db) {
        var stateFlag = getStateChecker(db,getFlagState());
        return Ability
                .builder()
                .name(name())
                .info(getInfo())
                .input(argsAmounts())
                .flag(add(getFlags(), stateFlag))
                .locality(USER);
    }
}
