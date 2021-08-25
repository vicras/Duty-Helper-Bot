package com.sbo.bot.handler.impl.management;

import com.sbo.bot.annotation.BotCommand;
import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.events.CallbackChatEvent;
import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.state.State;
import com.sbo.bot.state.impl.management.ManagementState;
import com.sbo.exception.DuringHandleExecutionException;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.AuthorizationService;
import com.sbo.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static com.sbo.domain.postgres.entity.enums.PersonRole.USER;
import static java.util.Objects.nonNull;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
@BotCommand
public class NewPersonHandler extends AbstractBaseHandler {

    private final PersonService personService;

    public NewPersonHandler(AuthorizationService authorizationService, ApplicationEventPublisher publisher,
                            CurrentPersonProvider personProvider, PersonService personService) {
        super(authorizationService, publisher, personProvider);
        this.personService = personService;
    }

    @Override
    protected void handleMessage(Update message) {
        Long newPersonId = (isConsistId(message)) ? idFromText(message) : idFromForward(message);

        throwIfUserExist(newPersonId);

        GetChat get = GetChat.builder().
                chatId(newPersonId.toString()).
                build();

        Consumer<Chat> handle = (Chat chat) ->
                personService.addEmptyPersonWithTelegramIdAndRole(newPersonId, Set.of(USER));

        Runnable error = () -> {
            throw new DuringHandleExecutionException(
                    List.of(InlineMessageBuilder.builder(personProvider.getCurrentPerson())
                            .line("Can't find user with provided id")
                            .build()),
                    "Can't find user with provided id");
        };

        publisher.publishEvent(CallbackChatEvent.of(get, handle, error));

        publishOkMessage();
    }

    private void publishOkMessage() {
        publish(InlineMessageBuilder.builder(personProvider.getCurrentPerson())
                .line("Person add successfully👌")
                .build());
    }

    private void throwIfUserExist(Long newPersonId) {
        if (personService.isPersonExist(newPersonId)) {
            SendMessage message = InlineMessageBuilder.builder(personProvider.getCurrentPerson())
                    .line("Person with id=%s already exist", newPersonId)
                    .build();
            throw new DuringHandleExecutionException(List.of(message), String.format("Person with id=%s already exist", newPersonId));
        }
    }

    @NotNull
    private Long idFromForward(Update message) {
        Long newPersonId;
        newPersonId = Long.valueOf(message.getMessage().getForwardFrom().getId());
        return newPersonId;
    }

    @NotNull
    private Long idFromText(Update message) {
        return Long.valueOf(message.getMessage().getText());
    }

    @Override
    public boolean canProcessMessage(Update update) {
        return update.hasMessage() && (isConsistId(update) || isForwardFrom(update));
    }

    private boolean isForwardFrom(Update update) {
        return nonNull(update.getMessage().getForwardFrom());
    }

    private boolean isConsistId(Update update) {
        return StringUtils.isNumeric(update.getMessage().getText());
    }

    @Override
    public Class<? extends State> getNextState() {
        return ManagementState.class;
    }
}
