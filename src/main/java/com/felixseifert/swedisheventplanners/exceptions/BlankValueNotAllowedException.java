package com.felixseifert.swedisheventplanners.exceptions;

public class BlankValueNotAllowedException extends IllegalArgumentException {

    public BlankValueNotAllowedException(String exception) {
        super(exception);
    }
}