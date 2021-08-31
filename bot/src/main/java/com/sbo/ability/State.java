package com.sbo.ability;

import com.sbo.ability.states.StartState;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.util.AbilityExtension;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static org.telegram.abilitybots.api.util.AbilityUtils.getUser;

/**
 * @author viktar hraskou
 */
@Component
public interface State extends AbilityExtension {
    String STATE_WORD = "states";
    State IDENTITY_STATE = new State() {
    };

    static void stateSetter(@NotNull MessageContext context, @NotNull Class<? extends State> clazz) {
        Long userId = context.user().getId();
        context.bot().db().<Long, Class<? extends State>>getMap(STATE_WORD)
                .put(userId, clazz);
    }

    static Class<? extends State> stateGetter(@NotNull User user, @NotNull DBContext db) {
        return db.<Long, Class<? extends State>>getMap(STATE_WORD)
                .getOrDefault(user.getId(), StartState.class);
    }

    static Predicate<Update> getStateChecker(DBContext db, Class<? extends State> expected) {
        return update -> {
            var user = getUser(update);
            return Objects.equals(expected, (stateGetter(user, db)));
        };
    }

    static Predicate<Update> getStateChecker(DBContext db, List<Class<? extends State>> oneExpected) {
        return update -> {
            var user = getUser(update);
            Class<? extends State> aClass = stateGetter(user, db);
            return oneExpected.contains(aClass);
        };
    }
}
