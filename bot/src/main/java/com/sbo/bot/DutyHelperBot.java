package com.sbo.bot;

import com.sbo.ability.chain.UpdateProcessor;
import com.sbo.bot.events.CallbackChatEvent;
import com.sbo.bot.events.UpdateCreationEvent;
import com.sbo.common.CreationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.util.AbilityExtension;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
public class DutyHelperBot extends AbilityBot {
    private final ApplicationEventPublisher publisher;

    private final int botCreator;
    private final List<UpdateProcessor> globalProcessors;

    protected DutyHelperBot(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.name}") String botName,
            @Value("${telegram.bot.creator}") int botCreator,
            List<UpdateProcessor> globalProcessors,
            ApplicationEventPublisher publisher) {
        super(botToken, botName);
        this.publisher = publisher;
        this.botCreator = botCreator;
        this.globalProcessors = globalProcessors;

    }

    @Override
    protected boolean checkGlobalFlags(Update update) {
        return UpdateProcessor.updateProcessorAppender(globalProcessors).test(update);
    }

    @Override
    public long creatorId() {
        return botCreator;
    }

    public void addExtension(AbilityExtension extension){
        super.addExtension(extension);
        onRegister();
    }

    @Override
    public CompletableFuture<List<Message>> executeAsync(SendMediaGroup sendMediaGroup) {
        return super.executeAsync(sendMediaGroup);
    }

    public Ability messageReceived(){
        return Ability.builder()
                .name(DEFAULT)
                .locality(USER)
                .privacy(PUBLIC)
                .action(this::newUpdate)
                .build();
    }

    public void newUpdate(MessageContext context) {
        publisher.publishEvent(new UpdateCreationEvent(context.update()));
    }

    @EventListener
    public void executeSafe(CreationEvent<BotApiMethod<?>> event) {
        final var message = event.getObject();
        try {
            execute(message);
            log.info("Executed {}", message);
        } catch (TelegramApiException e) {
            log.error("Exception while sending message {} \nException: {}", message, e.toString());
        }
    }

    @EventListener
    public void executeSafe(SendPhoto event) {
        try {
            execute(event);
            log.info("Executed {}", event);
        } catch (TelegramApiException e) {
            log.error("Exception while sending message {} \nException: {}", event, e.toString());
        }
    }

    @EventListener
    public void executeWithCallback(CallbackChatEvent event) {
        final var message = event.getMethod();
        final var handle = event.getHandle();
        final var error = event.getError();
        try {
            var result = execute(message);
            handle.accept(result);
            log.info("Executed {}", message);
        } catch (TelegramApiException e) {
            error.run();
        }
    }

}
