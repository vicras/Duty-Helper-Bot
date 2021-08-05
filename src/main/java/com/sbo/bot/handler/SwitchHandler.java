package com.sbo.bot.handler;

import com.sbo.bot.annotation.BotCommand;
import com.sbo.bot.handler.impl.enums.ButtonCommands;
import com.sbo.bot.state.State;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.function.Predicate;

import static java.util.Objects.nonNull;

/**
 * @author viktar hraskou
 */
@BotCommand
public class SwitchHandler implements BaseHandler {

    private List<ButtonCommands> qualifiers;
    private Predicate<Update> predicate;
    private final Class<? extends State> nextState;

    public static SwitchHandler of(Class<? extends State> nextState, ButtonCommands... command) {
        return new SwitchHandler(List.of(command), nextState);
    }

    public static SwitchHandler of(Class<? extends State> nextState, Predicate<Update> canProcess) {
        return new SwitchHandler(canProcess, nextState);
    }

    private SwitchHandler(List<ButtonCommands> qualifiers, Class<? extends State> nextState) {
        this.qualifiers = qualifiers;
        this.nextState = nextState;
    }

    private SwitchHandler(Predicate<Update> predicate, Class<? extends State> nextState) {
        this.predicate = predicate;
        this.nextState = nextState;
    }

    @Override
    public void authorizeAndHandle(Update update) {
    }

    @Override
    public Class<? extends State> getNextState() {
        return nextState;
    }

    @Override
    public boolean canProcessMessage(Update update) {
        return nonNull(qualifiers) ? checkCommand(update) : predicate.test(update);
    }

    public boolean checkCommand(Update update) {
        if(update.hasCallbackQuery()){
            var text = update.getCallbackQuery().getData();
            return nonNull(text) && isCommand(text) && qualifiers.contains(extractCommand(text));
        }
        return false;
    }

    protected ButtonCommands extractCommand(String text) {
        return ButtonCommands.valueOf(text);
    }

    private boolean isCommand(String text) {
        try {
            ButtonCommands.valueOf(text);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
