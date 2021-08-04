package com.sbo.bot.state.impl.settings;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.handler.SwitchBaseHandler;
import com.sbo.bot.security.AuthorizationService;
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

import static com.sbo.bot.handler.impl.enums.ButtonCommands.ADDRESS;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.BIRTH;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.LANGUAGE;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.LASTNAME;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.MAIL;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.NAME;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.PATRONYMIC;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.TELEPHONE;
import static java.util.stream.Collectors.toList;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
public class SettingState extends State {

    private final AuthorizationService authorizationService;

    private List<AbstractBaseHandler> handlers;

    public SettingState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher,
                        PersonService personService, AuthorizationService authorizationService
    ) {
        super(personProvider, publisher, personService);
        this.authorizationService = authorizationService;

        initHandlers();
    }


    private void initHandlers() {
        handlers = mapCommandWithState().stream()
                .map(commandToState -> new SwitchBaseHandler(authorizationService, publisher, personProvider, commandToState))
                .collect(toList());

    }

    private List<SwitchBaseHandler.CommandToState> mapCommandWithState() {
        return List.of(
                new SwitchBaseHandler.CommandToState(NAME, NameWaitingState.class),
                new SwitchBaseHandler.CommandToState(LASTNAME, LastNameWaitingState.class),
                new SwitchBaseHandler.CommandToState(PATRONYMIC, PatronymicWaitingState.class),
                new SwitchBaseHandler.CommandToState(TELEPHONE, TelephoneWaitingState.class),
                new SwitchBaseHandler.CommandToState(ADDRESS, HomeAddressWaitingState.class),
                new SwitchBaseHandler.CommandToState(MAIL, EmailWaitingState.class),
                new SwitchBaseHandler.CommandToState(BIRTH, BirthdayWaitingState.class),
                new SwitchBaseHandler.CommandToState(LANGUAGE, LanguageWaitingState.class)
        );
    }

    @Override
    protected List<AbstractBaseHandler> getAvailableHandlers() {
        return handlers;
    }

    @Override
    protected RequestOperator getRequestOperator(Update update) {
        var mess = getStatusMessage(update);

        return new RequestOperator(publisher)
                .addMessage(mess, update);
    }

    private SendMessage getStatusMessage(Update update) {
        Person person = personProvider.getCurrentPerson();

        var messBuilder = InlineMessageBuilder.builder(person)
                .header("Settings")
                .line("Name: %s", person.getFirstName())
                .line("Last name: %s", person.getLastName())
                .line("Patronymic: %s", person.getPatronymic())
                .line("Telephone: %s", person.getTel())
                .line("Home address: %s", person.getHomeAddress())
                .line("Birthday: %s", person.getBirthDate())
                .line("Mail: %s", person.getMail())
                .line("Language: %s", person.getLanguage())
                .line("Current roles:");

        person.getRoles().forEach(role -> messBuilder.line("- %s", role));

        return messBuilder.line("You can edit current setting, chose what do you want to update:")
                .row()
                .button("Name", NAME)
                .button("Last name", LASTNAME)
                .button("Patronymic", PATRONYMIC)
                .row()
                .button("Address", ADDRESS)
                .button("Birth", BIRTH)
                .button("Mail", MAIL)
                .row()
                .button("Telephone", TELEPHONE)
                .button("Language", LANGUAGE)
                .build();
    }
}
