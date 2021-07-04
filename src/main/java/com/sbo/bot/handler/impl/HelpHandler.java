package com.sbo.bot.handler.impl;

import com.sbo.bot.annotation.BotCommand;
import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.enums.Command;
import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.handler.CommandBaseHandler;
import com.sbo.bot.handler.impl.enums.ButtonCommands;
import com.sbo.bot.security.AuthorizationService;
import com.sbo.bot.state.State;
import com.sbo.entity.Person;
import com.sbo.provider.CurrentPersonProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * Shows help message and dynamically created inline keyboard based on user role
 * <p>
 * Available to: everyone
 */
@Slf4j
@Component
public class HelpHandler extends CommandBaseHandler {

    private final List<AbstractBaseHandler> handlers;
    @Value("${telegram.bot.name}")
    private String botUsername;

    public HelpHandler(AuthorizationService authorizationService, ApplicationEventPublisher publisher, CurrentPersonProvider personProvider, List<AbstractBaseHandler> handlers) {
        super(authorizationService, publisher, personProvider);
        this.handlers = handlers;
    }

    @Override
    protected List<ButtonCommands> getCommandQualifiers() {
        return null;
    }

    @Override
    public State getNextState() {
        return null;
    }

    @Override
    public void handleMessage(Update update) {

    }


    protected void handle(Person user, String message) {
        InlineMessageBuilder builder = InlineMessageBuilder.builder(user)
                .line("Hello. I'm *%s*", botUsername)
                .line("Here are your available commands")
                .line("Use [/help] command to display this message");

        // Dynamically creates buttons if handler has message and is available to user
        for (AbstractBaseHandler handler : handlers) {
            BotCommand annotation = handler.getClass().getAnnotation(BotCommand.class);
            Command command = annotation.command()[0];
            String description = annotation.command()[0].getCommandDescription();
            if (!description.isEmpty() && authorizationService.authorize(handler.getClass(), user)) {
                builder.row()
                        .button(description, command);
            }
        }

        publish(builder.build());
    }
}
