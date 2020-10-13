package com.felixseifert.swedisheventplanners.exceptions;

public class EntityAlreadyExistsException extends IllegalArgumentException {

    public EntityAlreadyExistsException(String exception) {
        super(exception);
    }
}