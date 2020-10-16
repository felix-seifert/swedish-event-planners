package com.felixseifert.swedisheventplanners.backend.exceptions;

public class ValueNotAllowedException extends IllegalArgumentException {

    public ValueNotAllowedException(String exception) {
        super(exception);
    }
}