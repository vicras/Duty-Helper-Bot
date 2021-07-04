package com.sbo.bot.handler.impl;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.security.AuthorizationService;
import com.sbo.bot.state.State;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.context.ApplicationEventPublisher;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author viktar hraskou
 */
public class HomeAddressHandler extends AbstractBaseHandler {

    private final PersonService personService;

    public HomeAddressHandler(AuthorizationService authorizationService, ApplicationEventPublisher publisher, CurrentPersonProvider personProvider, PersonService personService) {
        super(authorizationService, publisher, personProvider);
        this.personService = personService;
    }

    @Override
    protected void handleMessage(Update message) {
        String address = extractStringText(message);
        personService.updatePersonHomeAddress(personProvider.getCurrentPersonId(), address);
        publishOkMessage(address);
    }

    @Override
    public boolean canProcessMessage(Update update) {
        return update.getMessage().hasText();
    }

    private void publishOkMessage(String address) {
        SendMessage message = InlineMessageBuilder.builder(personProvider.getCurrentPersonId())
                .header("Address _%s_ set successfully:)", address)
                .build();
        publish(message);
    }

    @Override
    public State getNextState() {
        // TODO return Settings state
        throw new NotYetImplementedException();
    }
}
