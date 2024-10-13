package com.task.thinkon.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class UniqueConstraintViolationException extends RuntimeException {
    private Map<String, String> errors;

    public UniqueConstraintViolationException(Map<String, String> errors) {
        super(buildErrorMessage(errors));
        this.errors = errors;
    }

    private static String buildErrorMessage(Map<String, String> errors) {
        StringBuilder message = new StringBuilder("Unique constraint violations: ");
        errors.forEach((field, error) -> message.append(String.format("[%s: %s] ", field, error)));
        return message.toString();
    }
}

