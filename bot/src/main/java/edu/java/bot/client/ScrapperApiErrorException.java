package edu.java.bot.client;

import lombok.Getter;

@Getter
public class ScrapperApiErrorException extends RuntimeException {
    private final String code;

    private final String description;

    public ScrapperApiErrorException(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
