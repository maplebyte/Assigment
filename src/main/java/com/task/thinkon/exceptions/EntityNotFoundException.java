package com.task.thinkon.exceptions;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Long id) {
        super("User with id " + id + " not found");
    }
}