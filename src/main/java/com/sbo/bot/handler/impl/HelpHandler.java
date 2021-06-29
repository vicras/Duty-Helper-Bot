package com.sbo.bot.handler.impl;

import com.sbo.bot.annotation.BotCommand;
import com.sbo.bot.builder.MessageBuilder;
import com.sbo.bot.enums.Command;
import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.security.AuthorizationService;
import com.sbo.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static com.sbo.bot.enums.Command.HELP;

/**
 * Shows help message and dynamically created inline keyboard based on user role
 * <p>
 * Available to: everyone
 */
@Slf4j
@BotCommand(command = {HELP})
public class HelpHandler extends AbstractBaseHandler {

    private final List<AbstractBaseHandler> handlers;
    @Value("${telegram.bot.name}")
    private String botUsername;

    public HelpHandler(AuthorizationService authorizationService,
                       ApplicationEventPublisher publisher,
                       List<AbstractBaseHandler> handlers) {
        super(authorizationService, publisher);
        this.handlers = handlers;
    }

    @Override
    protected void handle(Person user, String message) {
        MessageBuilder builder = MessageBuilder.builder(user)
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
