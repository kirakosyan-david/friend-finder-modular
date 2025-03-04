package com.friendfinder.friendfindercommon.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DeleteFriendNotFoundException extends RuntimeException {
    public DeleteFriendNotFoundException() {
        super();
    }

    public DeleteFriendNotFoundException(String message) {
        super(message);
    }

    public DeleteFriendNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteFriendNotFoundException(Throwable cause) {
        super(cause);
    }
}
