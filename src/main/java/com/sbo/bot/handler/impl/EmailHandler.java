package com.sbo.bot.handler.impl;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.security.AuthorizationService;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
public class EmailHandler extends ProfileSettingHandler {
    private final EmailValidator emailValidator;
    private final PersonService personService;

    public EmailHandler(AuthorizationService authorizationService, ApplicationEventPublisher publisher, CurrentPersonProvider personProvider, EmailValidator emailValidator, PersonService personService) {
        super(authorizationService, publisher, personProvider);
        this.emailValidator = emailValidator;
        this.personService = personService;
    }

    @Override
    protected void handleMessage(Update message) {
        String email = extractStringText(message);
        personService.updatePersonMail(personProvider.getCurrentPersonId(), email);
        publishOkMessage(email);
    }

    private void publishOkMessage(String email) {
        SendMessage message = InlineMessageBuilder.builder(personProvider.getCurrentPersonId())
                .header("Email _%s_ set successfully:)", email)
                .build();
        publish(message);
    }

    @Override
    public boolean canProcessMessage(Update update) {
        return emailValidator.isValid(extractStringText(update), null);
    }
}
