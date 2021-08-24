package com.sbo.bot.state.impl.timetable;

import com.sbo.bot.builder.ChangeHolderPrinter;
import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.handler.BaseHandler;
import com.sbo.bot.handler.SwitchHandler;
import com.sbo.bot.state.RequestOperator;
import com.sbo.bot.state.State;
import com.sbo.bot.state.impl.HomeState;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.ChangeRequestService;
import com.sbo.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.function.Consumer;

import static com.sbo.bot.handler.impl.enums.ButtonCommands.DISMISS;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.SEND;

/**
 * @author viktar hraskou
 */
@Slf4j
@Component
public class SendRequestState extends State {

    private final ChangeHolderPrinter holderPrinter;
    private final ChangeRequestService changeRequestService;
    private final Consumer<Update> backAction;

    public SendRequestState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher,
                            PersonService personService, ChangeHolderPrinter holderPrinter,
                            ChangeRequestService changeRequestService) {
        super(personProvider, publisher, personService);
        this.holderPrinter = holderPrinter;
        this.changeRequestService = changeRequestService;

        backAction = update -> this.changeRequestService.deleteDataForPerson(personProvider.getCurrentPerson());
    }

    @Override
    protected List<BaseHandler> getAvailableHandlers() {
        return List.of(
                SwitchHandler.of(HomeState.class, DISMISS)
                        .setAction(backAction),
                SwitchHandler.of(TimetableState.class, SEND)
                        .setAction(this::sendChangeRequest)
        );
    }

    @Override
    protected RequestOperator getRequestOperator(Update update) {
        var sendMessage = getSendMessage(update);
        return new RequestOperator(publisher)
                .addMessage(sendMessage, update);
    }

    private SendMessage getSendMessage(Update update) {
        var builder = InlineMessageBuilder.builder(personProvider.getCurrentPerson())
                .header("You can send this request to change:")
                .line("")
                .row()
                .button("Send", SEND)
                .button("Dismiss", DISMISS);
        holderPrinter.fillMessageWithDataHolderData(builder);
        return builder.build();
    }

    private void sendChangeRequest(Update update) {
        changeRequestService.sendRequestIfComplete();
    }
}
