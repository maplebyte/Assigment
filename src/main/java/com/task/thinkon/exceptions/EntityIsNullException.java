package com.task.thinkon.exceptions;

public class EntityIsNullException extends RuntimeException {

    public EntityIsNullException() {
        super("Provided entity is null");
    }
}