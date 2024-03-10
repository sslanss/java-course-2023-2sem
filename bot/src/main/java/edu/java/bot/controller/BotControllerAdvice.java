package edu.java.bot.controller;

import edu.java.exceptions.BadRequestException;
import edu.java.responses.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BotControllerAdvice {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(BadRequestException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(e.getResponseMessage(),
            e.getHttpStatus(), e.getClass().getName(), null, null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errorResponse);
    }
}
