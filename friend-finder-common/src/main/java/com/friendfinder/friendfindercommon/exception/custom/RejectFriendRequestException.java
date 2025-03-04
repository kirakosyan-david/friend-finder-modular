package com.friendfinder.friendfindercommon.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RejectFriendRequestException extends RuntimeException {
    public RejectFriendRequestException() {
        super();
    }

    public RejectFriendRequestException(String message) {
        super(message);
    }

    public RejectFriendRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RejectFriendRequestException(Throwable cause) {
        super(cause);
    }
}
