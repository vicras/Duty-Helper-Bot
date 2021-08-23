package com.sbo.bot.state.impl.timetable;

import com.sbo.bot.builder.calendar.CalendarProvider;
import com.sbo.bot.builder.pagination.DutyPagination;
import com.sbo.bot.handler.BaseHandler;
import com.sbo.bot.handler.SwitchHandler;
import com.sbo.bot.state.RequestOperator;
import com.sbo.bot.state.State;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.sbo.bot.handler.impl.enums.ButtonCommands.BACK;

/**
 * @author viktar hraskou
 */
@Component
public class DayState extends State {

    private final DutyPagination dutyPagination;

    public DayState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher, PersonService personService, DutyPagination dutyPagination) {
        super(personProvider, publisher, personService);
        this.dutyPagination = dutyPagination;
    }

    @Override
    protected List<BaseHandler> getAvailableHandlers() {
        return List.of(
                SwitchHandler.of(TimetableState.class, BACK),
                SwitchHandler.of(getClass(), this::isPaginationCommand),
                SwitchHandler.of(DutyState.class, this::isDutyChosen)
        );
    }

    @Override
    protected RequestOperator getRequestOperator(Update update) {
        var sendMessage = dutyPagination.handleAll(update.getCallbackQuery());

        return new RequestOperator(publisher)
                .addMessage(sendMessage, update);
    }

    private boolean isPaginationCommand(Update update) {
        return update.hasCallbackQuery()
                && CalendarProvider.getIsChosenDay().test(update.getCallbackQuery().getData());
    }

    private boolean isDutyChosen(Update update) {
        return update.hasCallbackQuery()
                && DutyPagination.isDutyChosen.test(update.getCallbackQuery().getData());
    }

}
