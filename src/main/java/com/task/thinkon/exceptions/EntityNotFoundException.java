package com.task.thinkon.exceptions;

import java.util.UUID;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(UUID id) {
        super("User with id " + id + " not found");
    }
}