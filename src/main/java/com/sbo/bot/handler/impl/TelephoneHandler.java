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
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
public class TelephoneHandler extends ProfileSettingHandler {

    private final PersonService personService;

    public TelephoneHandler(AuthorizationService authorizationService, ApplicationEventPublisher publisher, CurrentPersonProvider personProvider, PersonService personService) {
        super(authorizationService, publisher, personProvider);
        this.personService = personService;
    }

    @Override
    protected void handleMessage(Update message) {
        long tel = extractTelNumber(message);
        personService.updatePersonTel(personProvider.getCurrentPersonId(), tel);
        publishOkMessage(tel);
    }

    private long extractTelNumber(Update update) {
        Message message = update.getMessage();
        String tel = message.hasContact()
                ? message.getContact().getPhoneNumber()
                : extractStringText(update);

        return parseTelNumber(extractStringText(update));
    }

    @Override
    public boolean canProcessMessage(Update update) {
        Message message = update.getMessage();
        return message.hasContact() || hasTelInText(message);
    }

    private boolean hasTelInText(Message message) {
        return message.hasText() && isTelNumber(message.getText());
    }

    private boolean isTelNumber(String parseTelNumber) {
        try {
            parseTelNumber(parseTelNumber);
            return true;
        } catch (NumberFormatException ignore) {
            return false;
        }
    }

    public long parseTelNumber(String text) {
        text = text.replaceAll("[(+\\- )]", "");

        var number = Long.parseLong(text);
        if (number > 100000000000L && number < 999999999999L) {
            return number;
        }

        throw new NumberFormatException();
    }

    private void publishOkMessage(long tel) {
        SendMessage message = InlineMessageBuilder.builder(personProvider.getCurrentPersonId())
                .header("Telephone _%s_ set successfully:)", tel)
                .build();
        publish(message);
    }
}
