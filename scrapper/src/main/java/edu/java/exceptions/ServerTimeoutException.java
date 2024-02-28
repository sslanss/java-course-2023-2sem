package edu.java.exceptions;

public class ServerTimeoutException extends Exception {
    public ServerTimeoutException() {
        super("Connection timeout exceeded");
    }
}
