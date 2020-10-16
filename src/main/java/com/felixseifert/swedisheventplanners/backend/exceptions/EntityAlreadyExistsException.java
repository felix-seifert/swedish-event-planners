package com.felixseifert.swedisheventplanners.backend.exceptions;

public class EntityAlreadyExistsException extends IllegalArgumentException {

    public EntityAlreadyExistsException(String exception) {
        super(exception);
    }
}