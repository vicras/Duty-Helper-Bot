package com.sbo.bot.state.impl.timetable;

import com.sbo.bot.builder.DutyMessagePrinter;
import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.builder.pagination.DutyPagination;
import com.sbo.bot.handler.BaseHandler;
import com.sbo.bot.handler.SwitchHandler;
import com.sbo.bot.state.RequestOperator;
import com.sbo.bot.state.State;
import com.sbo.domain.postgres.entity.Duty;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.DutyService;
import com.sbo.service.PersonService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.sbo.bot.handler.impl.enums.ButtonCommands.BACK;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.SWAP;
import static org.apache.commons.lang3.StringUtils.isNumeric;

/**
 * @author viktar hraskou
 */
@Component
public class DutyState extends State {

    private final DutyMessagePrinter messagePrinter;
    private final DutyService dutyService;

    public DutyState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher,
                     PersonService personService, DutyMessagePrinter messagePrinter,
                     DutyService dutyService) {
        super(personProvider, publisher, personService);
        this.messagePrinter = messagePrinter;
        this.dutyService = dutyService;
    }

    @Override
    protected List<BaseHandler> getAvailableHandlers() {
        return List.of(
                SwitchHandler.of(TimetableState.class, BACK),
                SwitchHandler.of(getClass(), this::isContainsSwap),
                SwitchHandler.of(TimeWaitingState.class, this::isContainsPersonOnDutyId)
        );
    }

    @Override
    protected RequestOperator getRequestOperator(Update update) {
        Long dutyId = DutyPagination.parseDutyId.apply(update.getCallbackQuery().getData());
        Duty duty = dutyService.getDutyById(dutyId);
        return new RequestOperator(publisher)
                .addMessage(dutyInfoMessage(duty, update), update);
    }

    private SendMessage dutyInfoMessage(Duty duty, Update update) {
        InlineMessageBuilder builder = InlineMessageBuilder.builder(personProvider.getCurrentPerson());
        messagePrinter.simpleDutyMessage(builder, duty);
        return setCommandButtons(builder, duty, update)
                .build();
    }

    private InlineMessageBuilder setCommandButtons(InlineMessageBuilder builder, Duty duty, Update update) {
        if (isContainsSwap(update)) {
            messagePrinter.addButtonsWithPersonOnDuty(builder, duty);
        } else {
            builder.row()
                    .button("Swap", SWAP + ":" + duty.getId());
        }
        return builder
                .row()
                .button("Back", BACK);
    }

    private boolean isContainsSwap(Update update) {
        return update.getCallbackQuery().getData().contains(SWAP.name());
    }

    private boolean isContainsPersonOnDutyId(Update update) {
        return isNumeric(update.getCallbackQuery().getData());
    }
}
