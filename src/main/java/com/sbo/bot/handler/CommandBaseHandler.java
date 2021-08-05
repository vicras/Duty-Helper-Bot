package com.sbo.bot.handler;

import com.sbo.bot.handler.impl.enums.ButtonCommands;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.impl.AuthorizationServiceImpl;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static java.util.Objects.nonNull;

/**
 * @author Dmitars
 */
@Component
public abstract class CommandBaseHandler extends AbstractBaseHandler {

    public CommandBaseHandler(AuthorizationServiceImpl authorizationService, ApplicationEventPublisher publisher, CurrentPersonProvider personProvider) {
        super(authorizationService, publisher, personProvider);
    }

    @Override
    public boolean canProcessMessage(Update update) {
        if (update.hasCallbackQuery()) {
            var text = update.getCallbackQuery().getData();
            return nonNull(text) && isCommand(text) && getCommandQualifiers().contains(extractCommand(update));
        }
        return false;
    }

    protected ButtonCommands extractCommand(Update update) {
        String text = extractStringText(update);
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

    protected abstract List<ButtonCommands> getCommandQualifiers();

}
