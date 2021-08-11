package com.sbo.bot.state.impl.management;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.handler.BaseHandler;
import com.sbo.bot.handler.SwitchHandler;
import com.sbo.bot.state.RequestOperator;
import com.sbo.bot.state.State;
import com.sbo.bot.state.impl.StartState;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.util.function.Predicate.not;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
public class SelfDestructionState extends State {

    private final ApplicationContext applicationContext;

    public SelfDestructionState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher,
                                PersonService personService, ApplicationContext applicationContext) {
        super(personProvider, publisher, personService);
        this.applicationContext = applicationContext;
    }

    @Override
    protected List<BaseHandler> getAvailableHandlers() {
        return List.of(
                SwitchHandler.of(ManagementState.class, not(this::isWordDelete)),
                SwitchHandler.of(StartState.class, this::isWordDelete)
                        .setAction(this::blockUser)
        );
    }

    @Override
    protected RequestOperator getRequestOperator(Update update) {
        var mess = createWarnMessage();
        return new RequestOperator(publisher)
                .addMessage(mess, update);
    }

    private SendMessage createWarnMessage() {
        return InlineMessageBuilder.builder(personProvider.getCurrentPerson())
                .line("To delete account send word \"DELETE\" in reply for this message")
                .line("Write everything else to back")
                .header("Warning!!!")
                .line("Only admin of Bot could restore account")
                .setForceReply()
                .build();

    }

    private boolean isWordDelete(Update update) {
        return update.hasMessage()
                && "delete".equalsIgnoreCase(update.getMessage().getText());
    }

    private void blockUser(Update update) {
        personService.blockPersonByTelegramId(personProvider.getCurrentPersonId());
        publisher.publishEvent(goodByeMessage());
    }

    private SendPhoto goodByeMessage() {
        InputStream fPicture = getFPicture();
        return SendPhoto.builder()
                .chatId(personProvider.getCurrentPersonId().toString())
                .photo(new InputFile(fPicture, "Bye samurai✊"))
                .build();
    }

    @NotNull
    private InputStream getFPicture() {
        InputStream fPicture;
        try {
            fPicture = applicationContext.getResource("classpath:img/press-f-pay-respects-nus.jpg")
                    .getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fPicture;
    }
}
