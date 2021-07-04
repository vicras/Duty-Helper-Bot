package com.sbo.bot.events;

import com.sbo.common.CreationEvent;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

/**
 * @author viktar hraskou
 */
public class ApiMethodsCreationEvent extends CreationEvent<BotApiMethod<?>> {

    public ApiMethodsCreationEvent(BotApiMethod<?> object) {
        super(object);
    }

    public static ApiMethodsCreationEvent of(BotApiMethod<?> object) {
        return new ApiMethodsCreationEvent(object);
    }


}
