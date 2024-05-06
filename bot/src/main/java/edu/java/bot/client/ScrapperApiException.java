package edu.java.bot.client;

import lombok.Getter;

@Getter
public class ScrapperApiException extends RuntimeException {
    private final String code;

    private final String description;

    public ScrapperApiException(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
