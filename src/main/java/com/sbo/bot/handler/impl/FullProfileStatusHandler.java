package com.sbo.bot.handler.impl;

import com.sbo.bot.annotation.BotCommand;
import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.enums.Command;
import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.security.AuthorizationService;
import com.sbo.bot.state.State;
import com.sbo.entity.Person;
import com.sbo.provider.CurrentPersonProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.sbo.entity.enums.PersonRole.UNAUTHORIZED;

/**
 * @author viktar hraskou
 */


@BotCommand(command = Command.EMPTY, requiredRoles = {UNAUTHORIZED})
public class FullProfileStatusHandler extends AbstractBaseHandler {

    public FullProfileStatusHandler(AuthorizationService authorizationService, ApplicationEventPublisher publisher, CurrentPersonProvider personProvider) {
        super(authorizationService, publisher, personProvider);
    }

    private SendMessage createMessageInfo(Person person) {

        //text
        var messageBuilder = InlineMessageBuilder.builder(person.getTelegramId().toString())
                .line("Current settings:")
                .line("First name: %s", person.getFirstName())
                .line("Last name: %s", person.getLastName())
                .line("Patronymic: %s", person.getPatronymic())
                .line("Birthday: %s", person.getBirthDate())
                .line("Telephone: %s", person.getTel())
                .line("Mail address: %s", person.getMail())
                .line("Home address: %s", person.getHomeAddress())
                .line("Roles: ");
        person.getRoles().forEach(role -> messageBuilder.line("- %s", role));

//        buttons
        return messageBuilder
                .row()
                .button("first name", "/first_name")
                .button("last name", "/last_name")
                .row()
                .button("patronymic", "/patronymic")
                .build();
    }


    @Override
    protected void handleMessage(Update message) {
        var user = personProvider.getCurrentPerson();
        var mess = createMessageInfo(user);
        publish(mess);
    }

    @Override
    public boolean canProcessMessage(Update update) {
        return false;
    }

    @Override
    public State getNextState() {
        return null;
    }
}
