package com.sbo.bot.handler.impl.settings;

import com.sbo.bot.annotation.BotCommand;
import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.security.AuthorizationService;
import com.sbo.bot.state.State;
import com.sbo.bot.state.impl.settings.SettingState;
import com.sbo.exception.DuringHandleExecutionException;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.sbo.bot.handler.impl.enums.ButtonCommands.BACK;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
@BotCommand
public class LastNameHandler extends AbstractBaseHandler {

    private final String NAME_REGEX = "[\\w-А-Яа-я]+";
    private final PersonService personService;

    public LastNameHandler(AuthorizationService authorizationService, ApplicationEventPublisher publisher,
                           CurrentPersonProvider personProvider, PersonService personService) {
        super(authorizationService, publisher, personProvider);
        this.personService = personService;
    }

    @Override
    protected void handleMessage(Update message) {
        String lastName = extractStringText(message).strip();
        if (lastName.matches(NAME_REGEX)) {
            Long userId = personProvider.getCurrentPerson().getTelegramId();
            personService.updatePersonLastName(userId, lastName);
            publishOkMessage();
        } else {
            throwDuringExecutionException();
        }
    }

    private void throwDuringExecutionException() {
        String msg = "Can't change last name should match regex";
        SendMessage message = InlineMessageBuilder.builder(personProvider.getCurrentPersonId())
                .header("Can't change last name.")
                .line("Use only cyrillic and latin letters, numbers, _ , -.")
                .row()
                .button("Back", BACK)
                .build();

        throw new DuringHandleExecutionException(List.of(message), msg);
    }

    private void publishOkMessage() {
        SendMessage message = InlineMessageBuilder.builder(personProvider.getCurrentPersonId())
                .header("Last name set successfully:)")
                .build();
        publish(message);
    }

    @Override
    public boolean canProcessMessage(Update update) {
        return update.hasMessage();
    }

    @Override
    public Class<? extends State> getNextState() {
        return SettingState.class;
    }
}