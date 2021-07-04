package com.sbo.bot.handler.impl;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.security.AuthorizationService;
import com.sbo.bot.state.State;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static com.sbo.common.utils.DateTimeUtil.parseDate;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
public class BirthdayHandler extends AbstractBaseHandler {

    private final PersonService personService;

    public BirthdayHandler(AuthorizationService authorizationService, ApplicationEventPublisher publisher, CurrentPersonProvider personProvider, PersonService personService) {
        super(authorizationService, publisher, personProvider);
        this.personService = personService;
    }

    @Override
    protected void handleMessage(Update message) {
        var text = extractStringText(message);
        LocalDate birthDate = parseDate(text);
        personService.updatePersonBirth(personProvider.getCurrentPersonId(), birthDate);
        publishOkMessage(birthDate);
    }

    private void publishOkMessage(LocalDate birthDate) {
        SendMessage message = InlineMessageBuilder.builder(personProvider.getCurrentPersonId())
                .header("Birthday _%s_ set successfully:)", birthDate)
                .build();
        publish(message);
    }
    @Override
    public boolean canProcessMessage(Update update) {
        var text = extractStringText(update);
        try {
            parseDate(text);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    @Override
    public State getNextState() {
        // TODO return Settings state
        throw new NotYetImplementedException();
    }
}
