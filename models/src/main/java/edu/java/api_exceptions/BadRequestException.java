package edu.java.api_exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BadRequestException extends RuntimeException {
    private final String httpStatus;
    private final String responseMessage;

    public BadRequestException(String httpStatus, String responseMessage) {
        this.httpStatus = httpStatus;
        this.responseMessage = responseMessage;
    }

    public BadRequestException() {
        httpStatus = HttpStatus.BAD_REQUEST.toString();
        responseMessage = "Incorrect request parameters";
    }
}
