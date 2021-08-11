package com.sbo.bot.state.impl.timetable;

import com.sbo.bot.builder.calendar.CalendarProvider;
import com.sbo.bot.handler.BaseHandler;
import com.sbo.bot.handler.SwitchHandler;
import com.sbo.bot.state.RequestOperator;
import com.sbo.bot.state.State;
import com.sbo.bot.state.impl.HomeState;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.util.List;

import static com.sbo.bot.handler.impl.enums.ButtonCommands.HOME;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
public class TimetableState extends State {

    private final CalendarProvider calendarProvider;

    public TimetableState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher,
                          PersonService personService, CalendarProvider calendarProvider) {
        super(personProvider, publisher, personService);
        this.calendarProvider = calendarProvider;
    }

    @Override
    protected List<BaseHandler> getAvailableHandlers() {
        return List.of(
                SwitchHandler.of(HomeState.class, HOME),
                SwitchHandler.of(getClass(), this::isCalendarCommand)
        );
    }

    @Override
    protected RequestOperator getRequestOperator(Update update) {
        SendMessage sendMessage = calendarProvider.handle(update.getCallbackQuery(), personProvider.getCurrentPerson());

        return new RequestOperator(publisher)
                .addMessage(sendMessage, update);
    }

    private boolean isCalendarCommand(Update update) {
        return update.hasCallbackQuery()
                && calendarProvider.getIsCommand().test(update.getCallbackQuery().getData());

    }
}
