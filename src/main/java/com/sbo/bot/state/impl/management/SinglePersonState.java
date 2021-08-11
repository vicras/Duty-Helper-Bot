package com.sbo.bot.state.impl.management;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.handler.BaseHandler;
import com.sbo.bot.handler.SwitchHandler;
import com.sbo.bot.state.RequestOperator;
import com.sbo.bot.state.State;
import com.sbo.entity.Person;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.sbo.bot.handler.impl.enums.ButtonCommands.BACK;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.BLOCK;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.ROLES;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
public class SinglePersonState extends State {

    public SinglePersonState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher, PersonService personService) {
        super(personProvider, publisher, personService);
    }

    @Override
    protected List<BaseHandler> getAvailableHandlers() {
        return List.of(
                SwitchHandler.of(AllPersonState.class, BACK),
                SwitchHandler.of(BlockPersonState.class, this::isBlockRequest),
                SwitchHandler.of(RoleChangingState.class, this::isRolesRequest)
        );
    }

    @Override
    protected RequestOperator getRequestOperator(Update update) {
        var person = extractPerson(update);
        SendMessage message = printPersonInfo(person);
        return new RequestOperator(publisher)
                .addMessage(message);
    }

    public SendMessage printPersonInfo(Person person) {
        person = personService.initializePersonRoles(person);

        InlineMessageBuilder builder = InlineMessageBuilder.builder(personProvider.getCurrentPerson())
                .header("%s %s %s", person.getLastName(), person.getFirstName(), person.getPatronymic())
                .line("Tel: %s", person.getTel())
                .line("Address: %s", person.getHomeAddress())
                .line("Email: %s", person.getMail())
                .line("Birth: %s", person.getBirthDate())
                .line("Roles: ");
        person.getRoles().forEach(role -> builder.line("- %s", role));
        return builder.line("Link: [%s](tg://user?id=%d)", person.getFirstName(), person.getTelegramId())
                .line("You can block person or change his system role")
                .row()
                .button("Block", BLOCK + " " + person.getTelegramId())
                .button("Set Roles", ROLES + " " + person.getTelegramId())
                .button("Back", BACK)
                .build();
    }

    private Person extractPerson(Update update) {
        long id = Long.parseLong(update.getCallbackQuery().getData());
        return personService.getPersonByTelegramId(id);
    }

    private boolean isBlockRequest(Update update) {
        return update.hasCallbackQuery()
                && update.getCallbackQuery().getData().contains(BLOCK.name());
    }

    private boolean isRolesRequest(Update update) {
        return update.hasCallbackQuery()
                && update.getCallbackQuery().getData().contains(ROLES.name());
    }
}
