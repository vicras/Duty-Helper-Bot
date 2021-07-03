package com.sbo.entity.enums;

import lombok.Getter;

import java.util.Locale;

import static java.util.Locale.forLanguageTag;

/**
 * @author viktar hraskou
 */
public enum Language {
    ENGLISH(Locale.ENGLISH),
    RUSSIAN(forLanguageTag("ru"));

    @Getter
    private final Locale locale;

    Language(Locale tag) {
        this.locale = tag;
    }
}
