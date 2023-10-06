package com.example.exception;

import static com.example.message.ErrorMessage.ENTITY_NOT_FOUND;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Object propertyOfEntity) {
        super(ENTITY_NOT_FOUND.formatWith(propertyOfEntity));
    }
}
