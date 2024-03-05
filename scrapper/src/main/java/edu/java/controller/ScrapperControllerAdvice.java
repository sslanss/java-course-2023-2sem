package edu.java.controller;

import edu.java.dto.responses.ApiErrorResponse;
import edu.java.exceptions.AlreadyTrackedLinkException;
import edu.java.exceptions.BadRequestException;
import edu.java.exceptions.ChatNotFoundException;
import edu.java.exceptions.ChatReregisteringException;
import edu.java.exceptions.UntrackedLinkException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ScrapperControllerAdvice {
    @ExceptionHandler(ChatNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleChatNotFoundException(ChatNotFoundException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse("Chat does not exists",
            HttpStatus.NOT_FOUND.toString(), e.getClass().getName(), null, null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(errorResponse);
    }

    @ExceptionHandler(UntrackedLinkException.class)
    public ResponseEntity<ApiErrorResponse> handleLinkIsNotTrackedException(UntrackedLinkException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse("Link is not being tracked",
            HttpStatus.NOT_FOUND.toString(), e.getClass().getName(), null, null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(errorResponse);
    }

    @ExceptionHandler(AlreadyTrackedLinkException.class)
    public ResponseEntity<ApiErrorResponse> handleLinkIsAlreadyTrackedException(AlreadyTrackedLinkException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse("Link is already being tracked",
            HttpStatus.ALREADY_REPORTED.toString(), e.getClass().getName(), null, null
        );
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
            .body(errorResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(BadRequestException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse("Incorrect request",
            HttpStatus.BAD_REQUEST.toString(), e.getClass().getName(), null, null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errorResponse);
    }

    @ExceptionHandler(ChatReregisteringException.class)
    public ResponseEntity<ApiErrorResponse> handleChatReregisteringException(ChatReregisteringException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse("The chat has already been registered",
            HttpStatus.ALREADY_REPORTED.toString(), e.getClass().getName(), null, null
        );
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
            .body(errorResponse);
    }
}
