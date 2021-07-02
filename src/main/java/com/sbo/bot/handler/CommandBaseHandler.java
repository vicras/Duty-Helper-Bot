package com.sbo.bot.handler;

import com.sbo.bot.security.AuthorizationService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Dmitars
 */
@Component
public abstract class CommandBaseHandler extends AbstractBaseHandler {
    public CommandBaseHandler(AuthorizationService authorizationService, ApplicationEventPublisher publisher) {
        super(authorizationService, publisher);
    }

    @Override
    public boolean canProcessMessage(Update update) {
        String text = extractTest(update);
        return text != null && extractCommand(text).equals(getCommandQualifier());
    }

    private String extractCommand(String text) {
        return text.split(" ")[0];
    }

    protected abstract String getCommandQualifier();

}
