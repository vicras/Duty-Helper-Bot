package com.sbo.bot.state.impl.management;

import com.sbo.bot.builder.pagination.PersonPagination;
import com.sbo.bot.handler.BaseHandler;
import com.sbo.bot.handler.SwitchHandler;
import com.sbo.bot.state.RequestOperator;
import com.sbo.bot.state.State;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.sbo.bot.handler.impl.enums.ButtonCommands.BACK;
import static org.apache.commons.lang3.StringUtils.isNumeric;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
public class AllPersonState extends State {

    private final PersonPagination personPagination;
    private final SwitchHandler controlButtonsHandler = SwitchHandler.of(getClass(), this::isPageChange);
    private final SwitchHandler personButtonsHandler = SwitchHandler.of(SinglePersonState.class, this::isPersonChosen);

    public AllPersonState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher,
                          PersonService personService, PersonPagination personPagination) {
        super(personProvider, publisher, personService);
        this.personPagination = personPagination;
    }

    @Override
    protected List<BaseHandler> getAvailableHandlers() {
        return List.of(
                SwitchHandler.of(ManagementState.class, BACK),
                controlButtonsHandler,
                personButtonsHandler
        );
    }

    @Override
    protected RequestOperator getRequestOperator(Update update) {
        var mess = personPagination.paginateActivePersons(update.getCallbackQuery());

        return new RequestOperator(publisher)
                .addMessage(mess, update);
    }

    private boolean isPageChange(Update update) {
        return update.hasCallbackQuery()
                && personPagination.getIsPageChangeRequest().test(update.getCallbackQuery().getData());
    }

    private boolean isPersonChosen(Update update) {
        return update.hasCallbackQuery()
                && isNumeric(update.getCallbackQuery().getData());
    }


}
