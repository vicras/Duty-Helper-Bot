package com.sbo.bot.handler.impl.settings;

import com.sbo.bot.annotation.BotCommand;
import com.sbo.bot.builder.MessageBuilder;
import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.service.impl.AuthorizationServiceImpl;
import com.sbo.bot.state.State;
import com.sbo.bot.state.impl.settings.SettingState;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import lombok.extern.slf4j.Slf4j;
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
@BotCommand
public class TelephoneHandler extends AbstractBaseHandler {

    private final PersonService personService;

    public TelephoneHandler(AuthorizationServiceImpl authorizationService, ApplicationEventPublisher publisher,
                            CurrentPersonProvider personProvider, PersonService personService) {
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

        return parseTelNumber(tel);
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
        SendMessage message = MessageBuilder.builder(personProvider.getCurrentPersonId())
                .line("Telephone _%s_ set successfully:)", tel)
                .row()
                .withKeyboardDelete()
                .build();
        publish(message);
    }

    @Override
    public Class<? extends State> getNextState() {
        return SettingState.class;
    }
}
