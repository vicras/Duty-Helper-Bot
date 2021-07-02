package com.sbo.bot.messages;

import com.sbo.bot.messages.providers.MessageDataProvider;
import org.hibernate.sql.Update;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.function.Predicate;

/**
 * @author Dmitars
 */
public abstract class MessageHandler {
    public final MessageDataProvider parse(Message message){
        testMessage(message);
        return extractData(message);
    };

    protected abstract MessageDataProvider extractData(Message message);

    protected abstract Predicate<Message> getDataPredicate();

    private void testMessage(Message message){
        if(!getDataPredicate().test(message)){
            throw new UnsupportedOperationException("Error: expected data type wasn't found in the message");
        }
    }
}
