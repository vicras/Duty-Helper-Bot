package com.sbo.bot.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Chat;

import java.util.function.Consumer;

/**
 * @author viktar hraskou
 */
@Getter
@RequiredArgsConstructor(staticName = "of")
public class CallbackChatEvent {
    private final BotApiMethod<Chat> method;
    private final Consumer<Chat> handle;
    private final Runnable error;
}
