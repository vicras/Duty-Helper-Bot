package com.sbo.bot.messages;

import com.sbo.bot.annotation.BotCommand;
import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.messages.providers.HandlerDataProvider;
import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author Dmitars
 */
// TODO delete?
@Component
@RequiredArgsConstructor
public class CommandMessageHandler extends MessageHandler {

    private final List<AbstractBaseHandler> handlers;

    @Override
    protected HandlerDataProvider extractData(Message message) {
        return new HandlerDataProvider(getHandler(message.getText()));
    }

    private AbstractBaseHandler getHandler(String text) {
        return handlers.stream()
                .filter(handler -> isCommandHandledByHandler(text, handler))
                .findAny()
                .orElseThrow();
    }

    @Override
    protected Predicate<Message> getDataPredicate() {
        return Message::hasText;
    }

    private boolean isCommandHandledByHandler(String command, AbstractBaseHandler h) {
        throw new NotYetImplementedException();
//        return Stream.of(h.getClass()
//                .getAnnotation(BotCommand.class)
//                .command()
//        )
//                .anyMatch(c -> c.equalsIgnoreCase(command));
    }

    private boolean isBotAnnotationPresent(AbstractBaseHandler h) {
        return h.getClass()
                .isAnnotationPresent(BotCommand.class);
    }

    private String extractCommand(String text) {
        return text.split(" ")[0];
    }
}
