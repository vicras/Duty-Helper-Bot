package com.sbo.ability;

import com.sbo.ability.exceptions.BotMethodExecuteException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.isNull;

/**
 * @author viktar hraskou
 */
public interface MessageSenders {

    @NotNull
    @Contract(pure = true)
    static Consumer<MessageContext> tryToEditUpdateWithMessage(Function<MessageContext, SendMessage> methodFunction) {
        return context -> {
            var message = methodFunction.apply(context);
            var messageToSend = tryToEditUpdate(message, context.update());
            executeExceptionally(context, messageToSend);
        };
    }

    @NotNull
    @Contract(pure = true)
    static <T extends Serializable, Method extends BotApiMethod<T>> Consumer<MessageContext> tryToSendMethod(Function<MessageContext, Method> methodFunction) {
        return context -> {
            var method = methodFunction.apply(context);
            executeExceptionally(context, method);
        };
    }

    private static <T extends Serializable, Method extends BotApiMethod<T>> void executeExceptionally(@NotNull MessageContext context, Method method) {
        try {
            context.bot().sender().execute(method);
        } catch (TelegramApiException e) {
            throw new BotMethodExecuteException(e);
        }
    }

    private static BotApiMethod<? extends Serializable> tryToEditUpdate(SendMessage message, Update update) {
        return (isPertinentlyEditPreviousMessage(update) && isInlineMarkupOrNull(message))
                ? convertToEditText(message, update)
                : message;

    }

    private static boolean isInlineMarkupOrNull(@NotNull SendMessage message) {
        return isNull(message.getReplyMarkup())
                || message.getReplyMarkup() instanceof InlineKeyboardMarkup;
    }

    private static boolean isPertinentlyEditPreviousMessage(@NotNull Update update) {
        return update.hasCallbackQuery();
    }

    private static EditMessageText convertToEditText(@NotNull SendMessage method, @NotNull Update update) {
        return EditMessageText.builder()
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .text(method.getText())
                .chatId(method.getChatId())
                .entities(method.getEntities())
                .parseMode(method.getParseMode())
                .replyMarkup((InlineKeyboardMarkup) method.getReplyMarkup())
                .build();
    }

}
