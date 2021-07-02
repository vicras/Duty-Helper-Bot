package com.sbo.bot.messages.providers;

/**
 * @author Dmitars
 */
public abstract class MessageDataProvider<T> {
    T data;

    public MessageDataProvider(T data) {
        this.data = data;
    }

    T get(){
        return data;
    }

}
