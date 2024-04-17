package edu.java.api_exceptions;

import lombok.Getter;

@Getter public class ServerErrorException extends RuntimeException {
    private final int code;

    public ServerErrorException(int code) {
        this.code = code;
    }
}
