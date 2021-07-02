package com.sbo.bot.messages.providers;

import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.List;

/**
 * @author Dmitars
 */
public class PhotoDataProvider extends MessageDataProvider<List<PhotoSize>> {
    public PhotoDataProvider(List<PhotoSize> data) {
        super(data);
    }
}
