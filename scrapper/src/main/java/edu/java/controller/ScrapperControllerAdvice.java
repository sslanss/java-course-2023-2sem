package edu.java.controller;

import edu.java.api_exceptions.BadRequestException;
import edu.java.exceptions.tracker_exceptions.AlreadyTrackedLinkException;
import edu.java.exceptions.tracker_exceptions.ChatNotFoundException;
import edu.java.exceptions.tracker_exceptions.ChatReregisteringException;
import edu.java.exceptions.tracker_exceptions.UnsupportedLinkFormatException;
import edu.java.exceptions.tracker_exceptions.UntrackedLinkException;
import edu.java.responses.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ScrapperControllerAdvice {
    @ExceptionHandler(ChatNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleChatNotFoundException(ChatNotFoundException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse("Вы еще не зарегистрированы. ",
            HttpStatus.NOT_FOUND.toString(), e.getClass().getName(), null, null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(errorResponse);
    }

    @ExceptionHandler(UntrackedLinkException.class)
    public ResponseEntity<ApiErrorResponse> handleLinkIsNotTrackedException(UntrackedLinkException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse("Данная ссылка не отслеживается вами. ",
            HttpStatus.NOT_FOUND.toString(), e.getClass().getName(), null, null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(errorResponse);
    }

    @ExceptionHandler(UnsupportedLinkFormatException.class)
    public ResponseEntity<ApiErrorResponse> handleLinkFormatIsNotSupported(UnsupportedLinkFormatException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse("Ссылка неподдерживаемого формата. ",
            HttpStatus.UNSUPPORTED_MEDIA_TYPE.toString(), e.getClass().getName(), null, null
        );
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
            .body(errorResponse);
    }

    @ExceptionHandler(AlreadyTrackedLinkException.class)
    public ResponseEntity<ApiErrorResponse> handleLinkIsAlreadyTrackedException(AlreadyTrackedLinkException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse("Ссылка уже отслеживается вами. ",
            HttpStatus.CONFLICT.toString(), e.getClass().getName(), null, null
        );
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(errorResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(BadRequestException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(e.getResponseMessage(),
            e.getHttpStatus(), e.getClass().getName(), null, null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errorResponse);
    }

    @ExceptionHandler(ChatReregisteringException.class)
    public ResponseEntity<ApiErrorResponse> handleChatReregisteringException(ChatReregisteringException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse("Вы уже были зарегистрированы ранее. ",
            HttpStatus.CONFLICT.toString(), e.getClass().getName(), null, null
        );
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(errorResponse);
    }
}
