package com.sbo.bot.handler.impl;

import com.sbo.bot.builder.InlineMessageBuilder;
import com.sbo.bot.handler.AbstractBaseHandler;
import com.sbo.bot.state.State;
import com.sbo.bot.state.impl.StartState;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.impl.AuthorizationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.sbo.bot.handler.impl.enums.ButtonCommands.SETTINGS;

/**
 * @author viktar hraskou
 */
@Slf4j
public class StartHandler extends AbstractBaseHandler {

    public StartHandler(AuthorizationServiceImpl authorizationService, ApplicationEventPublisher publisher,
                        CurrentPersonProvider personProvider) {
        super(authorizationService, publisher, personProvider);
    }

    @Override
    protected void handleMessage(Update message) {
        SendMessage mess = InlineMessageBuilder.builder(personProvider.getCurrentPerson())
                .header("Hi")
                .line("You use simple duty helper telegram bot")
                .row()
                .button("Settings", SETTINGS)
                .build();
        publish(mess);
    }

    @Override
    public boolean canProcessMessage(Update update) {
        return extractStringText(update).trim().equals("/start");
    }

    @Override
    public Class<? extends State> getNextState() {
        return StartState.class;
    }

}
