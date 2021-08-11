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

import static java.util.function.Predicate.not;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
public class BlockPersonState extends State {

    public BlockPersonState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher, PersonService personService) {
        super(personProvider, publisher, personService);
    }

    @Override
    protected List<BaseHandler> getAvailableHandlers() {
        return List.of(
                SwitchHandler.of(AllPersonState.class, not(this::isWordBlock)),
                SwitchHandler.of(AllPersonState.class, this::isWordBlock)
                        .setAction(this::blockUser)
        );
    }

    @Override
    protected RequestOperator getRequestOperator(Update update) {
        var personToBlock = extractPerson(update);
        var mess = createWarnMessage(personToBlock);
        return new RequestOperator(publisher)
                .addMessage(mess, update);
    }

    private SendMessage createWarnMessage(Person personToBlock) {
        return InlineMessageBuilder.builder(personProvider.getCurrentPerson())
                .line("To block [%s](tg://user?id=%d) send word \"BLOCK\" in reply for this message",
                        personToBlock.getFirstName(), personToBlock.getTelegramId())
                .line("Write everything else to back")
                .setForceReply()
                .build();

    }

    private Person extractPerson(Update update) {
        long id = Long.parseLong(update.getCallbackQuery().getData().split(" ")[1]);
        return personService.getPersonByTelegramId(id);
    }

    private boolean isWordBlock(Update update) {
        return update.hasMessage()
                && "block".equalsIgnoreCase(update.getMessage().getText());
    }

    private void blockUser(Update update) {
        Long idToBlock = getIdToBlock(update);
        personService.blockPersonByTelegramId(idToBlock);
        publish(okMessage());
    }

    private Long getIdToBlock(Update update) {
        return Long.valueOf(update.getMessage().getReplyToMessage().getEntities().get(0).getUser().getId());
    }

    private SendMessage okMessage() {
        return InlineMessageBuilder.builder(personProvider.getCurrentPerson())
                .line("Blocked successfully✔")
                .build();
    }

}
