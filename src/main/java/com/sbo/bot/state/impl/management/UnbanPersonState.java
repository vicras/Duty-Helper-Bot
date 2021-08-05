package com.sbo.bot.state.impl.management;

import com.sbo.bot.builder.pagination.PersonPagination;
import com.sbo.bot.handler.BaseHandler;
import com.sbo.bot.handler.SwitchHandler;
import com.sbo.bot.state.RequestOperator;
import com.sbo.bot.state.State;
import com.sbo.entity.Person;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.sbo.bot.handler.impl.enums.ButtonCommands.BACK;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.PAGE;
import static org.apache.commons.lang3.StringUtils.isNumeric;

/**
 * @author viktar hraskou
 */
@Component
public class UnbanPersonState extends State {
    private final PersonPagination personPagination;
    private final SwitchHandler controlButtonsHandler = SwitchHandler.of(getClass(), this::isPageChange);

    public UnbanPersonState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher,
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
        int pageToPaginate = 0;
        if (isPageChange(update)) {
            pageToPaginate = getPageToPaginate(update);
        }

        var mess = personPagination.paginateBlockedPersons(pageToPaginate);

        return new RequestOperator(publisher)
                .addMessage(mess, update);
    }

    private int getPageToPaginate(Update update) {
        return Integer.parseInt(update.getCallbackQuery().getData().split(" ")[1]);
    }

    private boolean isPageChange(Update update) {
        return update.hasCallbackQuery()
                && update.getCallbackQuery().getData().contains(PAGE.name());
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
                .text(String.format("Person [%s](tg://user?id=%d) unblocked successfully", person.getFirstName(), person.getTelegramId()))
                .showAlert(true)
                .callbackQueryId(update.getCallbackQuery().getId())
                .build();

        publish(message);
    }
}
