package com.sbo.bot.state;

import com.sbo.bot.events.ApiMethodsCreationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitars
 */
public class RequestOperator {

    private final ApplicationEventPublisher publisher;
    protected List<BotApiMethod<?>> messages = new ArrayList<>();

    public RequestOperator(ApplicationEventPublisher publisher) {
        this.publisher = publisher;

    }

    public RequestOperator addMessage(BotApiMethod<?> messageBotApiMethod) {
        messages.add(messageBotApiMethod);
        return this;
    }

    public RequestOperator addMessage(SendMessage messageBotApiMethod, Update update) {
        if (isPertinentlyEditPreviousMessage(update)) {
            var editText = convertToEditText(messageBotApiMethod, update);
            messages.add(editText);
        } else {
            messages.add(messageBotApiMethod);
        }
        return this;
    }

    public void sendRequest() {
        for (var message : messages) {
            publish(message);
        }
    }

    private void publish(BotApiMethod<?> sendMessage) {
        publisher.publishEvent(ApiMethodsCreationEvent.of(sendMessage));
    }

    private boolean isPertinentlyEditPreviousMessage(Update update) {
        return messages.isEmpty() && update.hasCallbackQuery();
    }

    private EditMessageText convertToEditText(SendMessage method, Update update) {
        return EditMessageText.builder()
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .text(method.getText())
                .chatId(method.getChatId())
                .entities(method.getEntities())
                .parseMode(method.getParseMode())
//                    TODO ebanet?
                .replyMarkup((InlineKeyboardMarkup) method.getReplyMarkup())
                .build();
    }

}
