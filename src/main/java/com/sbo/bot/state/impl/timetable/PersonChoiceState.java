package com.sbo.bot.state.impl.timetable;

import com.sbo.bot.builder.DutyMessagePrinter;
import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.builder.calendar.CalendarProvider;
import com.sbo.bot.builder.pagination.DutyPagination;
import com.sbo.bot.handler.BaseHandler;
import com.sbo.bot.handler.SwitchHandler;
import com.sbo.bot.state.RequestOperator;
import com.sbo.bot.state.State;
import com.sbo.domain.postgres.entity.Duty;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.ChangeRequestService;
import com.sbo.service.DutyService;
import com.sbo.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

import static com.sbo.bot.builder.pagination.DutyPagination.parseDutyId;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.BACK;
import static java.time.DayOfWeek.MONDAY;
import static org.apache.commons.lang3.StringUtils.isNumeric;

/**
 * @author viktar hraskou
 */
// TODO think about divide into some small
@Slf4j
@Component
public class PersonChoiceState extends State {
    private final CalendarProvider calendarProvider;
    private final DutyPagination dutyPagination;
    private final Consumer<Update> backAction;
    private final DutyMessagePrinter messagePrinter;
    private final DutyService dutyService;
    private final ChangeRequestService changeRequestService;

    public PersonChoiceState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher,
                             PersonService personService, CalendarProvider calendarProvider,
                             DutyPagination dutyPagination, DutyMessagePrinter messagePrinter, ChangeRequestService changeRequestService, DutyService dutyService) {
        super(personProvider, publisher, personService);
        this.calendarProvider = calendarProvider;
        this.dutyPagination = dutyPagination;
        this.messagePrinter = messagePrinter;
        this.dutyService = dutyService;
        this.changeRequestService = changeRequestService;

        calendarProvider.setMapToText(this::calendarDateButtonsMapper);
        backAction = update -> this.changeRequestService.deleteDataForPerson(personProvider.getCurrentPerson());
    }

    @Override
    protected RequestOperator getRequestOperator(Update update) {
        return new RequestOperator(publisher)
                .addMessage(firstMessage(update), update);
    }

    private SendMessage firstMessage(Update update) {
        SendMessage message;
        if (isDutyChosen(update)) {
            message = dutyMessage(update);
        } else if (isDayChosenCommand(update)) {
            message = dayMessage(update);
        } else {
            message = calendarMessage(update);
        }
        return message;
    }

    private SendMessage dutyMessage(Update update) {
        Long dutyId = parseDutyId.apply(update.getCallbackQuery().getData());
        Duty duty = dutyService.getDutyById(dutyId);

        InlineMessageBuilder builder = InlineMessageBuilder.builder(personProvider.getCurrentPerson());
        messagePrinter.simpleDutyMessage(builder, duty);
        messagePrinter.addButtonsWithPersonOnDuty(builder, duty);
        return builder.build();
    }

    private SendMessage dayMessage(Update update) {
        if (changeRequestService.isPersonAlreadyFilledInHolder(personProvider.getCurrentPerson())) {
            return dutyPagination.handleWithoutDutyOfCurrentPerson(update.getCallbackQuery());
        }
        return dutyPagination.handleOnlyDutyOfCurrentPerson(update.getCallbackQuery());
    }

    private SendMessage calendarMessage(Update update) {
        return calendarProvider.handle(update.getCallbackQuery(), personProvider.getCurrentPerson());
    }

    private String calendarDateButtonsMapper(LocalDate day) {
//        TODO logic of non visible duty
        return MONDAY.equals(day.getDayOfWeek())
                ? "🏛 test"
                : String.valueOf(day.getDayOfMonth());
    }


    @Override
    protected List<BaseHandler> getAvailableHandlers() {
        return List.of(
                SwitchHandler.of(TimetableState.class, BACK)
                        .setAction(backAction),

                SwitchHandler.of(getClass(), this::isCalendarCommand),
                SwitchHandler.of(getClass(), this::isDayChosenCommand),

                SwitchHandler.of(getClass(), this::isPaginationCommand),
                SwitchHandler.of(getClass(), this::isDutyChosen),

                SwitchHandler.of(TimeWaitingState.class, this::isContainsPersonOnDutyId)
        );
    }

    // region calendar predicate
    private boolean isCalendarCommand(Update update) {
        return update.hasCallbackQuery()
                && calendarProvider.getIsCommand().test(update.getCallbackQuery().getData());

    }

    private boolean isPaginationCommand(Update update) {
        return update.hasCallbackQuery()
                && CalendarProvider.getIsChosenDay().test(update.getCallbackQuery().getData());
    }
    // endregion

    // region day predicate
    private boolean isDayChosenCommand(Update update) {
        return update.hasCallbackQuery()
                && CalendarProvider.getIsChosenDay().test(update.getCallbackQuery().getData());
    }

    private boolean isDutyChosen(Update update) {
        return update.hasCallbackQuery()
                && DutyPagination.isDutyChosen.test(update.getCallbackQuery().getData());
    }
    // endregion

    //region duty predicate
    private boolean isContainsPersonOnDutyId(Update update) {
        return isNumeric(update.getCallbackQuery().getData());
    }
    //endregion
}
