package edu.kravchenko.multithreading.exception;

public class SeaportException extends Exception {

    public SeaportException() {
    }

    public SeaportException(String message) {
        super(message);
    }

    public SeaportException(Throwable cause) {
        super(cause);
    }

    public SeaportException(String message, Throwable cause) {
        super(message, cause);
    }
}
