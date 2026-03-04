package com.uvod.user.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum that defines the locales supported by the platform.
 */
public enum SupportedLocale {

    IT("it-IT"),
    EN("en-US");

    private final String value;

    SupportedLocale(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static SupportedLocale fromValue(String value) {
        for (SupportedLocale locale : values()) {
            if (locale.value.equals(value)) {
                return locale;
            }
        }
        throw new IllegalArgumentException("Unsupported locale: " + value);
    }
}
