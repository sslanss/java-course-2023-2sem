package edu.java.exceptions;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
    private final String httpStatus;
    private final String responseMessage;

    public BadRequestException(String httpStatus, String responseMessage) {
        this.httpStatus = httpStatus;
        this.responseMessage = responseMessage;
    }
}
