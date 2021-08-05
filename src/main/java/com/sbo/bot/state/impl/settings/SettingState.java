package com.sbo.bot.state.impl.settings;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.handler.BaseHandler;
import com.sbo.bot.handler.SwitchHandler;
import com.sbo.bot.state.RequestOperator;
import com.sbo.bot.state.State;
import com.sbo.entity.Person;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import com.sbo.service.impl.AuthorizationServiceImpl;
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

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
public class SettingState extends State {

    private List<BaseHandler> handlers;

    public SettingState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher,
                        PersonService personService, AuthorizationServiceImpl authorizationService
    ) {
        super(personProvider, publisher, personService);

        initHandlers();
    }


    private void initHandlers() {
        handlers = List.of(
                SwitchHandler.of(NameWaitingState.class, NAME),
                SwitchHandler.of(LastNameWaitingState.class, LASTNAME),
                SwitchHandler.of(PatronymicWaitingState.class, PATRONYMIC),
                SwitchHandler.of(TelephoneWaitingState.class, TELEPHONE),
                SwitchHandler.of(HomeAddressWaitingState.class, ADDRESS),
                SwitchHandler.of(EmailWaitingState.class, MAIL),
                SwitchHandler.of(BirthdayWaitingState.class, BIRTH),
                SwitchHandler.of(LanguageWaitingState.class, LANGUAGE)
        );
    }

    @Override
    protected List<BaseHandler> getAvailableHandlers() {
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
                .line("Telephone: +%s", person.getTel())
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
