package com.sbo.bot.handler;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.events.SendMessageCreationEvent;
import com.sbo.bot.security.AuthorizationService;
import com.sbo.bot.state.State;
import com.sbo.entity.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

///**
// * Abstract class for all handlers
// * <p>
// * Inheritors are marked with {@link com.whiskels.notifier.telegram.annotation.BotCommand} annotation to define
// * supported command.
// * <p>
// * Scheduling of {@link #handle(User, String)} call is possible with
// * {@link com.whiskels.notifier.telegram.annotation.Schedulable} annotation
// */
@Slf4j
@Component
@RequiredArgsConstructor
public abstract class AbstractBaseHandler {

    protected final AuthorizationService authorizationService;
    protected final ApplicationEventPublisher publisher;

    public abstract boolean canProcessMessage(Update update);

    public abstract State getNextState();

    protected String extractTest(Update update){
        if(update.hasCallbackQuery())
            return update.getCallbackQuery().getData();
        else
            return update.getMessage().getText();
    }


    public final void publish(SendMessage message) {
        this.publisher.publishEvent(SendMessageCreationEvent.of(message));
    }

    /**
     * Performs authorization of user and handling of the command
     *
     * @param user    {@link} User that sent update to the bot
     * @param message {@link} content of the update
     */
    public final void authorizeAndHandle(Person user, String message) {
        if (this.authorizationService.authorize(this.getClass(), user)) {
            handle(user, message);
        } else {
            handleUnauthorized(user, message);
        }
    }

    public abstract void handleMessage(Message message);

    /**
     * Handling of the command if user is authorized
     */
    protected abstract void handle(Person user, String message);

    /**
     * Handling of the command if user is unauthorized
     */
    private void handleUnauthorized(Person user, String message) {
        log.info("Unauthorized access: {} {}", user, message);
        String userChatId = String.valueOf(user.getTelegramId());
        publish(InlineMessageBuilder.builder(userChatId)
                .line("Your token is *%s*", userChatId)
                .line("Please contact your supervisor to gain access")
                .build());
        publish(InlineMessageBuilder.builder("botAdmin")
                .line("*Unauthorized access:* %s", userChatId)
                .line("*Message:* %s", message == null || message.isEmpty()
                        ? "Empty"
                        : message.replaceAll("_", "-"))
                .build());
    }
}
