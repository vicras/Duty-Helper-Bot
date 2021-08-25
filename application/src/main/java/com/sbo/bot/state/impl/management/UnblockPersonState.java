package com.sbo.bot.state.impl.management;

import com.sbo.bot.builder.pagination.PersonPagination;
import com.sbo.bot.handler.BaseHandler;
import com.sbo.bot.handler.SwitchHandler;
import com.sbo.bot.state.RequestOperator;
import com.sbo.bot.state.State;
import com.sbo.domain.postgres.entity.Person;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.sbo.bot.handler.impl.enums.ButtonCommands.BACK;
import static org.apache.commons.lang3.StringUtils.isNumeric;

/**
 * @author viktar hraskou
 */
@Component
public class UnblockPersonState extends State {
    private final PersonPagination personPagination;
    private final SwitchHandler controlButtonsHandler = SwitchHandler.of(getClass(), this::isPageChange);

    public UnblockPersonState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher,
                              PersonService personService, PersonPagination personPagination) {
        super(personProvider, publisher, personService);
        this.personPagination = personPagination;
    }

    @Override
    protected List<BaseHandler> getAvailableHandlers() {
        return List.of(controlButtonsHandler,
                SwitchHandler.of(ManagementState.class, BACK),
                SwitchHandler.of(ManagementState.class, this::isExistingId)
                        .setAction(this::unbanAction)
        );
    }

    @Override
    protected RequestOperator getRequestOperator(Update update) {
        var mess = personPagination.paginateBlockedPersons(update.getCallbackQuery());

        return new RequestOperator(publisher)
                .addMessage(mess, update);
    }

    private boolean isPageChange(Update update) {
        return update.hasCallbackQuery()
                && personPagination.getIsPageChangeRequest().test(update.getCallbackQuery().getData());
    }

    private boolean isExistingId(Update update) {
        return update.hasCallbackQuery()
                && isNumeric(update.getCallbackQuery().getData())
                && personService.isPersonExist(Long.valueOf(update.getCallbackQuery().getData()));
    }

    private void unbanAction(Update update) {
        Long telegramId = Long.valueOf(update.getCallbackQuery().getData());
        Person person = personService.unblockPersonByTelegramId(telegramId);

        AnswerCallbackQuery message = AnswerCallbackQuery.builder()
                .text(String.format("Person %s unblocked successfully", person.getFirstName()))
                .showAlert(true)
                .callbackQueryId(update.getCallbackQuery().getId())
                .build();

        publish(message);
    }
}
