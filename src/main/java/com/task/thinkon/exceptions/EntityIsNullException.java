package com.task.thinkon.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class EntityIsNullException extends RuntimeException {

    public EntityIsNullException() {
        super("Exception: Provided entity is null");
    }
}