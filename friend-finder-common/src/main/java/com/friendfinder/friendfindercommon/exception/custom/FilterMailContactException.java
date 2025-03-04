package com.friendfinder.friendfindercommon.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FilterMailContactException extends RuntimeException {
    public FilterMailContactException() {
        super();
    }

    public FilterMailContactException(String message) {
        super(message);
    }

    public FilterMailContactException(String message, Throwable cause) {
        super(message, cause);
    }

    public FilterMailContactException(Throwable cause) {
        super(cause);
    }
}
