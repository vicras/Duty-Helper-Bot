package com.sbo.bot.state.impl.timetable;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.handler.BaseHandler;
import com.sbo.bot.handler.SwitchHandler;
import com.sbo.bot.handler.impl.timetable.TimeRangeHandler;
import com.sbo.bot.state.RequestOperator;
import com.sbo.bot.state.State;
import com.sbo.domain.postgres.entity.PeopleOnDuty;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.ChangeRequestService;
import com.sbo.service.PersonOnDutyService;
import com.sbo.service.PersonService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

import static com.sbo.bot.handler.impl.enums.ButtonCommands.BACK;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.FULL;

/**
 * @author viktar hraskou
 */
@Component
public class TimeWaitingState extends State {

    private final PersonOnDutyService personOnDutyService;
    private final TimeRangeHandler timeRangeHandler;
    private final ChangeRequestService changeRequestService;
    private final Consumer<Update> backAction;


    public TimeWaitingState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher,
                            PersonService personService, PersonOnDutyService personOnDutyService,
                            TimeRangeHandler timeRangeHandler, ChangeRequestService changeRequestService) {
        super(personProvider, publisher, personService);
        this.personOnDutyService = personOnDutyService;
        this.timeRangeHandler = timeRangeHandler;
        this.changeRequestService = changeRequestService;
        backAction = update -> changeRequestService.deleteDataForPerson(personProvider.getCurrentPerson());
    }

    @Override
    protected List<BaseHandler> getAvailableHandlers() {
        return List.of(
                SwitchHandler.of(TimetableState.class, BACK)
                        .setAction(backAction),
                timeRangeHandler
        );
    }

    @Override
    protected RequestOperator getRequestOperator(Update update) {
        PeopleOnDuty personOnDuty = getPersonOnDuty(update);
        updateChangeRequest(personOnDuty);

        SendMessage sendMessage = timeWaitingMessage(personOnDuty);
        return new RequestOperator(publisher)
                .addMessage(sendMessage, update);
    }

    private SendMessage timeWaitingMessage(PeopleOnDuty personOnDuty) {
        return InlineMessageBuilder.builder(personProvider.getCurrentPerson())
                .line(String.format("- %s time: %s - %s",
                        personOnDuty.getPerson().telegramLink(),
                        personOnDuty.getFromTime().format(DateTimeFormatter.ISO_DATE_TIME),
                        personOnDuty.getToTime().format(DateTimeFormatter.ISO_TIME)))
                .header("Choose the time period")
                .line("You can send a period in format")
                .line("-  hh:mm-hh:mm")
                .line("-  hh:mm hh:mm")
                .row()
                .button("FULL", FULL)
                .button("Back", BACK)
                .build();
    }

    private PeopleOnDuty getPersonOnDuty(Update update) {
        long personOnDutyId = Long.parseLong(update.getCallbackQuery().getData());
        return personOnDutyService.getPeopleOnDutyById(personOnDutyId);
    }

    private void updateChangeRequest(PeopleOnDuty personOnDuty) {
        changeRequestService.updateChangeRequest(personOnDuty);
    }

}
