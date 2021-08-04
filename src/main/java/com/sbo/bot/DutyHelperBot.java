package com.sbo.bot;

import com.sbo.bot.events.UpdateCreationEvent;
import com.sbo.common.CreationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DutyHelperBot extends TelegramLongPollingBot {

    private final ApplicationEventPublisher publisher;
    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public void onUpdateReceived(Update update) {

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

    @Override
    public String getBotUsername() {
        return null;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void printLogSeparator() {
        String stars = Stream.generate(() -> "*")
                .limit(34)
                .collect(joining());
        log.info(stars + "NEW REQUEST" + stars);
    }
}
