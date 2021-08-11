package com.sbo.bot.state.impl;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.handler.BaseHandler;
import com.sbo.bot.handler.SwitchHandler;
import com.sbo.bot.state.RequestOperator;
import com.sbo.bot.state.State;
import com.sbo.bot.state.impl.management.ManagementState;
import com.sbo.bot.state.impl.settings.SettingState;
import com.sbo.bot.state.impl.timetable.TimetableState;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.sbo.bot.handler.impl.enums.ButtonCommands.TIMETABLE;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.PERSON_MANAGEMENT;
import static com.sbo.bot.handler.impl.enums.ButtonCommands.SETTINGS;

/**
 * @author viktar hraskou
 */
@Component
public class HomeState extends State {
    public HomeState(CurrentPersonProvider personProvider, ApplicationEventPublisher publisher, PersonService personService) {
        super(personProvider, publisher, personService);
    }

    @Override
    protected List<BaseHandler> getAvailableHandlers() {
        return List.of(
                SwitchHandler.of(SettingState.class, SETTINGS),
                SwitchHandler.of(ManagementState.class, PERSON_MANAGEMENT),
                SwitchHandler.of(TimetableState.class, TIMETABLE)
        );
    }

    @Override
    protected RequestOperator getRequestOperator(Update update) {
        var mess = getStatusMessage(update);

        return new RequestOperator(publisher)
                .addMessage(mess, update);
    }

    private SendMessage getStatusMessage(Update update) {
        return InlineMessageBuilder.builder(personProvider.getCurrentPerson())
                .header("Main menu")
                .row()
                .button("Settings⚙🛠", SETTINGS)
                .button("Person management📈", PERSON_MANAGEMENT)
                .button("Timetable🗓", TIMETABLE)
                .build();
    }
}
