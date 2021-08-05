package com.sbo.bot.handler;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.events.ApiMethodsCreationEvent;
import com.sbo.entity.Person;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.AuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public abstract class AbstractBaseHandler implements BaseHandler {

    protected final AuthorizationService authorizationService;
    protected final ApplicationEventPublisher publisher;
    protected final CurrentPersonProvider personProvider;

    @Override
    public void authorizeAndHandle(Update update) {
        Person currentPerson = personProvider.getCurrentPerson();

        if (this.authorizationService.authorize(this.getClass(), currentPerson)) {
            handleMessage(update);
        } else {
            handleUnauthorized(currentPerson);
        }

    }

    private void handleUnauthorized(Person user) {
        log.info("Unauthorized access: {} to {}", user, this.getClass().getSimpleName());

        publish(InlineMessageBuilder.builder(user.getTelegramId())
                .line("Your token is *%s*", user.getTelegramId())
                .line("Please contact your supervisor to gain access")
                .build());
        publish(InlineMessageBuilder.builder(user.getTelegramId())
                .line("*Unauthorized access:* %s", user.getTelegramId())
                .build());
    }

    protected String extractStringText(Update update) {
        if (update.hasCallbackQuery())
            return update.getCallbackQuery().getData();
        else
            return update.getMessage().getText();
    }

    protected final void publish(SendMessage message) {
        this.publisher.publishEvent(ApiMethodsCreationEvent.of(message));
    }

    protected abstract void handleMessage(Update message);

}
