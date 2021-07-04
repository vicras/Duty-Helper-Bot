package com.sbo.bot.handler.impl;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.security.AuthorizationService;
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
import static java.util.Objects.nonNull;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
public class FirstNameHandler extends ProfileSettingHandler {

    private final String NAME_REGEX = "[\\w-А-Яа-я]+";
    private final PersonService personService;

    public FirstNameHandler(AuthorizationService authorizationService, ApplicationEventPublisher publisher, CurrentPersonProvider personProvider, PersonService personService) {
        super(authorizationService, publisher, personProvider);
        this.personService = personService;
    }

    @Override
    protected void handleMessage(Update message) {
        String firstName = extractStringText(message).strip();
        if (firstName.matches(NAME_REGEX)) {
            Long userId = personProvider.getCurrentPerson().getTelegramId();
            personService.updatePersonName(userId, firstName);
            publishOkMessage();
        } else {
            publishParseErrorMessage();
        }
    }

    private void publishParseErrorMessage() {
        String msg = "Can't change name should match regex";
        SendMessage message = InlineMessageBuilder.builder(personProvider.getCurrentPersonId())
                .header("Can't change first name.")
                .line("Use only cyrillic and latin letters, numbers, _ , -.")
                .row()
                .button("Back", BACK.name())
                .build();
        throw new DuringHandleExecutionException(List.of(message), msg);
    }

    private void publishOkMessage() {
        SendMessage message = InlineMessageBuilder.builder(personProvider.getCurrentPersonId())
                .header("First name set successfully:)")
                .build();
        publish(message);
    }

    @Override
    public boolean canProcessMessage(Update update) {
        return nonNull(extractStringText(update));
    }
}
