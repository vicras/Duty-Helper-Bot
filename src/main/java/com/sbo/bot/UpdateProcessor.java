package com.sbo.bot;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.events.SendMessageCreationEvent;
import com.sbo.bot.events.UpdateCreationEvent;
import com.sbo.bot.orchestrator.HandlerOrchestrator;
import com.sbo.entity.Person;
import com.sbo.exception.EntityNotFoundException;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Main class used to handle incoming Updates.
 * Verifies incoming update and delegates handling to {@link HandlerOrchestrator}
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateProcessor {

    protected final ApplicationEventPublisher publisher;
    private final HandlerOrchestrator orchestrator;
    private final CurrentPersonProvider personProvider;
    private final PersonService personService;

    @EventListener()
    public void handleUpdate(UpdateCreationEvent updateCreationEvent) {
        var update = updateCreationEvent.getObject();
        var userId = extractUserId(update);

        try {
            personProvider.setPersonById(userId);
            orchestrator.operate(update);
        } catch (EntityNotFoundException ex) {
            sendNotFoundMessage(userId);
        }

    }

    private long extractUserId(Update update) {
        long userId = 0;
        if (isMessageWithText(update)) {
            var message = update.getMessage();
            userId = message.getFrom().getId();
            var text = message.getText();
            log.debug("Update is text message {} from {}", text, userId);
        } else if (update.hasCallbackQuery()) {
            var callbackQuery = update.getCallbackQuery();
            userId = callbackQuery.getFrom().getId();
            var text = callbackQuery.getData();
            log.debug("Update is callbackQuery {} from {}", text, userId);
        }
        return userId;
    }

    private boolean isMessageWithText(Update update) {
        return !update.hasCallbackQuery() && update.hasMessage() && update.getMessage().hasText();
    }

    private void sendNotFoundMessage(Long telegramId) {
        var admins = personService.getActiveAdmins();
        var messBuilder = InlineMessageBuilder.builder(telegramId.toString())
                .line("*Error!!!*")
                .line("You do not have permission to use this bot.");

        if (!admins.isEmpty()) {
            messBuilder
                    .line("Contact with:")
                    .line();
            admins.forEach(person -> addPersonLinkToMessage(person, messBuilder));
        } else {
            messBuilder
                    .line("There are no people you can contact at the moment!");
        }
        this.publisher.publishEvent(SendMessageCreationEvent.of(messBuilder.build()));
    }

    private void addPersonLinkToMessage(Person person, InlineMessageBuilder builder) {
        builder.line("- [%s](tg://user?id=%d)", person.getFirstName(), person.getTelegramId());
    }

}
