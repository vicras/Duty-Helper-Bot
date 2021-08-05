package com.sbo.bot.handler;

import com.sbo.bot.state.State;
import com.sbo.entity.Person;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author viktar hraskou
 */
public interface BaseHandler {

    void authorizeAndHandle(Update update);

    boolean canProcessMessage(Update update);

    Class<? extends State> getNextState();
}
