package com.sbo.bot;

import com.sbo.bot.events.CallbackChatEvent;
import com.sbo.bot.events.UpdateCreationEvent;
import com.sbo.common.CreationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
public class DutyHelperBot extends AbilityBot {
    private final ApplicationEventPublisher publisher;

    private final int botCreator;

    protected DutyHelperBot(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.name}") String botName,
            @Value("${telegram.bot.creator}") int botCreator,
            ApplicationEventPublisher publisher) {
        super(botToken, botName);
        this.publisher = publisher;
        this.botCreator = botCreator;
    }

    @Override
    public long creatorId() {
        return botCreator;
    }
    public Reply messageReceived(){
        return Reply.of(this::newUpdate, update -> true);

    }

    public void newUpdate(BaseAbilityBot bot,Update update) {
        printLogSeparator();
        log.info("Received {}", update);
        publisher.publishEvent(new UpdateCreationEvent(update));
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

    public void printLogSeparator() {
        String stars = Stream.generate(() -> "*")
                .limit(34)
                .collect(joining());
        log.info(stars + "NEW REQUEST" + stars);
    }
}
