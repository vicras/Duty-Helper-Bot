package com.sbo.common.localization;

import com.sbo.provider.CurrentPersonProvider;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * @author viktar hraskou
 */
@Component
@AllArgsConstructor
public class LocalizeMessage {

    private final MessageSource messageSource;
    private final CurrentPersonProvider personProvider;

    public String getText(String key, String... args) {
        var currentPerson = personProvider.getCurrentPerson();
        var locale = currentPerson.getLanguage().getLocale();
        return messageSource.getMessage(key, args, locale);
    }

    public String getTextOrElse(String key, String defaultMessage, String... args) {
        var currentPerson = personProvider.getCurrentPerson();
        var locale = currentPerson.getLanguage().getLocale();
        return messageSource.getMessage(key, args, defaultMessage, locale);
    }
}
