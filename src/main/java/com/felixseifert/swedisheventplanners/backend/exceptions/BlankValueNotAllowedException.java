package com.felixseifert.swedisheventplanners.backend.exceptions;

public class BlankValueNotAllowedException extends IllegalArgumentException {

    public BlankValueNotAllowedException(String exception) {
        super(exception);
    }
}