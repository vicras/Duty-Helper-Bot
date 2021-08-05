package com.sbo.bot.handler.impl.settings;

import com.sbo.bot.annotation.BotCommand;
import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.service.impl.AuthorizationServiceImpl;
import com.sbo.bot.state.State;
import com.sbo.bot.state.impl.settings.SettingState;
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
@BotCommand
public class EmailHandler extends AbstractBaseHandler {

    private final EmailValidator emailValidator;
    private final PersonService personService;

    public EmailHandler(AuthorizationServiceImpl authorizationService, ApplicationEventPublisher publisher,
                        CurrentPersonProvider personProvider, PersonService personService) {
        super(authorizationService, publisher, personProvider);
        this.emailValidator = new EmailValidator();
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

    @Override
    public Class<? extends State> getNextState() {
        return SettingState.class;
    }
}
