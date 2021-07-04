package com.sbo.bot.handler;

import com.sbo.bot.handler.impl.enums.ButtonCommands;
import com.sbo.bot.security.AuthorizationService;
import com.sbo.provider.CurrentPersonProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author Dmitars
 */
@Component
public abstract class CommandBaseHandler extends AbstractBaseHandler {

    public CommandBaseHandler(AuthorizationService authorizationService, ApplicationEventPublisher publisher, CurrentPersonProvider personProvider) {
        super(authorizationService, publisher, personProvider);
    }

    @Override
    public boolean canProcessMessage(Update update) {
        String text = extractStringText(update);
        return text != null && getCommandQualifiers().contains(extractCommand(update));
    }

    protected ButtonCommands extractCommand(Update update) {
        String text = extractStringText(update);
        return ButtonCommands.valueOf(text);
    }

    protected abstract List<ButtonCommands> getCommandQualifiers();

}
