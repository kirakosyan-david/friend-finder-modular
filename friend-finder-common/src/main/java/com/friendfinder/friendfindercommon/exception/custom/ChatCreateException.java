package com.friendfinder.friendfindercommon.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ChatCreateException extends RuntimeException {
    public ChatCreateException() {
        super();
    }

    public ChatCreateException(String message) {
        super(message);
    }

    public ChatCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChatCreateException(Throwable cause) {
        super(cause);
    }
}
