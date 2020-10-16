package com.felixseifert.swedisheventplanners.exceptions;

public class ValueNotAllowedException extends IllegalArgumentException {

    public ValueNotAllowedException(String exception) {
        super(exception);
    }
}