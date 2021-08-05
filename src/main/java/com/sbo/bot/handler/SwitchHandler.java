package com.sbo.bot.handler;

import com.sbo.bot.annotation.BotCommand;
import com.sbo.bot.handler.impl.enums.ButtonCommands;
import com.sbo.bot.state.RequestOperator;
import com.sbo.bot.state.State;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.util.Objects.nonNull;

/**
 * @author viktar hraskou
 */
@BotCommand
public class SwitchHandler implements BaseHandler {

    private final Class<? extends State> nextState;
    private List<ButtonCommands> qualifiers;
    private Predicate<Update> predicate;
    private RequestOperator operator;
    private Consumer<Update> action;

    public SwitchHandler(Predicate<Update> canProcess, Class<? extends State> nextState, RequestOperator operator) {
        this.predicate = canProcess;
        this.nextState = nextState;
        this.operator = operator;
    }

    public SwitchHandler(List<ButtonCommands> commands, Class<? extends State> nextState, RequestOperator operator) {
        this.qualifiers = commands;
        this.nextState = nextState;
        this.operator = operator;
    }

    public static SwitchHandler of(Class<? extends State> nextState, ButtonCommands... command) {
        return new SwitchHandler(List.of(command), nextState, null);
    }

    public static SwitchHandler of(Class<? extends State> nextState, Predicate<Update> canProcess) {
        return new SwitchHandler(canProcess, nextState, null);
    }

    public static SwitchHandler of(Class<? extends State> nextState, RequestOperator operator, ButtonCommands... command) {
        return new SwitchHandler(List.of(command), nextState, operator);
    }

    public static SwitchHandler of(Class<? extends State> nextState, RequestOperator operator, Predicate<Update> canProcess) {
        return new SwitchHandler(canProcess, nextState, operator);
    }

    public SwitchHandler setOperator(RequestOperator operator) {
        this.operator = operator;
        return this;
    }

    public SwitchHandler setAction(Consumer<Update> action) {
        this.action = action;
        return this;
    }

    @Override
    public void authorizeAndHandle(Update update) {
        if (nonNull(action)) {
            action.accept(update);
        }
        if (nonNull(operator)) {
            operator.sendRequest();
        }
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
        if (update.hasCallbackQuery()) {
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
