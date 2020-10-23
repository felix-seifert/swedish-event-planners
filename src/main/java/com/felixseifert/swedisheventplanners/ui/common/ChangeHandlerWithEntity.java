package com.felixseifert.swedisheventplanners.ui.common;

import com.felixseifert.swedisheventplanners.backend.model.AbstractEntity;

public interface ChangeHandlerWithEntity {
    void onChange(AbstractEntity entity);
}