package com.felixseifert.swedisheventplanners.backend.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Preference {
    DECORATIONS("Decorations", 1),
    PARTIES("Parties", 2),
    PHOTOS_FILMING("Photos/filming", 3),
    BREAKFAST_LUNCH_DINNER("Breakfast, lunch, dinner", 4),
    SOFT_HOT_DRINK("Soft/hot drinks", 5);

    @Getter
    private final String name;

    @Getter
    private final Integer databaseCode;
}
