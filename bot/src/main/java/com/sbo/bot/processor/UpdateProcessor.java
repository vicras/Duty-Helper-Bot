package com.sbo.bot.processor;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author viktar hraskou
 */
public interface UpdateProcessor {
    int getPriority();
    void process(Update update);
}
