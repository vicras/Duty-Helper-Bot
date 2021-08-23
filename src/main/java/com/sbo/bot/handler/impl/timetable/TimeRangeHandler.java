package com.sbo.bot.handler.impl.timetable;

import com.google.common.collect.Range;
import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.state.State;
import com.sbo.bot.state.impl.HomeState;
import com.sbo.bot.state.impl.timetable.PersonChoiceState;
import com.sbo.bot.state.impl.timetable.SendRequestState;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.AuthorizationService;
import com.sbo.service.ChangeRequestService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalTime;

import static com.sbo.bot.handler.impl.enums.ButtonCommands.FULL;
import static com.sbo.common.utils.DateTimeUtil.isTimeRange;
import static com.sbo.common.utils.DateTimeUtil.parseTimeRange;

/**
 * @author viktar hraskou
 */
@Component
public class TimeRangeHandler extends AbstractBaseHandler {
    private final ChangeRequestService changeRequestService;

    public TimeRangeHandler(AuthorizationService authorizationService, ApplicationEventPublisher publisher,
                            CurrentPersonProvider personProvider, ChangeRequestService changeRequestService) {
        super(authorizationService, publisher, personProvider);
        this.changeRequestService = changeRequestService;
    }

    @Override
    protected void handleMessage(Update message) {
        if (isFullCommand(message)) {
            changeRequestService.useFullRange();
        } else {
            Range<LocalTime> localTimeRange = parseTimeRange(extractCallbackData(message));
            changeRequestService.updateChangeRequestWithSameDay(localTimeRange);
        }

    }

    @Override
    public boolean canProcessMessage(Update update) {
        return isCallbackDataExist(update)
                && (isFullCommand(update) || isTime(update));
    }

    @Override
    public Class<? extends State> getNextState() {
        if (changeRequestService.isDataComplete()) {
            changeRequestService.sendRequestIfComplete();
            return SendRequestState.class;
        } else {
            return PersonChoiceState.class;
        }
    }

    private boolean isFullCommand(Update update) {
        return FULL.name().equals(extractCallbackData(update));
    }

    private boolean isTime(Update update) {
        String text = extractMessageText(update);
        return isTimeRange(text);
    }


}
