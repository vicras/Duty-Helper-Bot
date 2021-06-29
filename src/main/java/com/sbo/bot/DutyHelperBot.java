package com.sbo.bot;

import com.sbo.bot.events.SendMessageCreationEvent;
import com.sbo.bot.events.UpdateCreationEvent;
import com.sbo.common.CreationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
        log.info("Received {}", update);
        publisher.publishEvent(new UpdateCreationEvent(update));
    }

    @EventListener(SendMessageCreationEvent.class)
    public void executeSafe(CreationEvent<SendMessage> event) {
        final SendMessage message = event.getObject();
        try {
            execute(message);
            log.debug("Executed {}", message);
        } catch (TelegramApiException e) {
            log.error("Exception while sending message {} to user: {}", message, e.getMessage());
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
}
