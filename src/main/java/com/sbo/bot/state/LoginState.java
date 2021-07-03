package com.sbo.bot.state;

import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.handler.impl.ActionHandler;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

/**
 * @author Dmitars
 */
@Component
public class LoginState extends State {
    private final RequestOperator requestOperator;
    private final ActionHandler actionHandler;

    public LoginState(PersonService personService, CurrentPersonProvider personProvider, ActionHandler actionHandler, RequestOperator requestOperator) {
        super(personProvider, personService);
        this.actionHandler = actionHandler;
        this.requestOperator = requestOperator;
    }

    @Override
    protected List<AbstractBaseHandler> getAvailableHandlers() {
        return List.of(actionHandler);
    }

    @Override
    protected RequestOperator getRequestOperator() {
        SendMessage sendMessage = SendMessage.builder()
                .text("Hello! It is a bot, that can help u with many HSS (СБО) needs and abilities")
                .text("\n")
                .text("Bot mostly provides GUI, but you have some default commands:")
                .text("/help - prints this message again")
                .text("/exit - returns you to that moment")
                .text("Please note, that you cannot use commands when your data is expected. Even exit.")
                .text("\n")
                .text("If it is the first time you join this bot, it will force you to fill all profile information,")
                .text("then you can go to profile editing by /update command or use its another functionality")
                .text("Nice research!")
                .build();
        requestOperator.addMessage(sendMessage);
        return requestOperator;
    }
}
