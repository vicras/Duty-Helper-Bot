package com.sbo.ability.states;

import com.sbo.ability.State;
import com.sbo.ability.annotations.StateFirst;
import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.domain.postgres.entity.Person;
import com.sbo.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.sbo.bot.handler.impl.enums.ButtonCommands.ADDRESS;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.BIRTH;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.HOME;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.LANGUAGE;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.LASTNAME;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.MAIL;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.NAME;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.PATRONYMIC;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.TELEPHONE;

/**
 * @author viktar hraskou
 */
@Component
@RequiredArgsConstructor
public class SettingState implements State {

    private final PersonService personService;

    @StateFirst
    public SendMessage getStatusMessage(MessageContext context) {
        Person person = personService.getPersonByTelegramId(context.chatId());

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
                .button("Address🗺", ADDRESS)
                .button("Birth🎂", BIRTH)
                .button("Mail📨", MAIL)
                .row()
                .button("Telephone📞", TELEPHONE)
                .button("Language🇧🇾", LANGUAGE)
                .row()
                .button("Home🏠", HOME)
                .build();
    }
}
