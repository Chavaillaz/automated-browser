package com.chavaillaz.browser.exception;

public class BrowserException extends RuntimeException {

    public BrowserException() {
        super();
    }

    public BrowserException(String message) {
        super(message);
    }

    public BrowserException(String message, Throwable cause) {
        super(message, cause);
    }

    public BrowserException(Throwable cause) {
        super(cause);
    }

}
