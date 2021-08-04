package com.sbo.bot.handler.impl.settings;

import com.sbo.bot.annotation.BotCommand;
import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.security.AuthorizationService;
import com.sbo.bot.state.State;
import com.sbo.bot.state.impl.settings.SettingState;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import lombok.extern.slf4j.Slf4j;
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
public class HomeAddressHandler extends AbstractBaseHandler {

    private final PersonService personService;

    public HomeAddressHandler(AuthorizationService authorizationService, ApplicationEventPublisher publisher,
                              CurrentPersonProvider personProvider, PersonService personService) {
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
    public Class<? extends State> getNextState() {
        return SettingState.class;
    }
}
