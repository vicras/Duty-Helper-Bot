package com.sbo.bot.state;

import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.handler.impl.ActionHandler;
import com.sbo.bot.handler.impl.HelpHandler;
import com.sbo.provider.CurrentPersonProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Dmitars
 */
@Component
public class LoginState extends State {
    @Autowired
    private HelpHandler helpHandler;

    @Autowired
    private ActionHandler actionHandler;

    public LoginState(CurrentPersonProvider personProvider) {
        super(personProvider);
    }


    @Override
    protected List<AbstractBaseHandler> getAvailableHandlers() {
        return List.of(helpHandler, actionHandler);
    }

    @Override
    protected RequestOperator getRequestOperator() {
        return null;
    }
}
