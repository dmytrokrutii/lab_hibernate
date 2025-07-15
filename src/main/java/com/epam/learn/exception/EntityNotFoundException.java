package com.epam.learn.exception;

import com.epam.learn.model.EntityType;

import java.util.UUID;

public class EntityNotFoundException extends RuntimeException {
    private static final String MESSAGE = "%s with id %s not found";

    public EntityNotFoundException(EntityType entity, UUID id) {
        super(MESSAGE.formatted(entity.name(), id));
    }
}
