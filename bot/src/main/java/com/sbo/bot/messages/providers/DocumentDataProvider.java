package com.sbo.bot.messages.providers;

import org.telegram.telegrambots.meta.api.objects.Document;

/**
 * @author Dmitars
 */
public class DocumentDataProvider extends MessageDataProvider<Document> {
    public DocumentDataProvider(Document data) {
        super(data);
    }
}
