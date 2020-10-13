package com.felixseifert.swedisheventplanners.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Preference {
    DECORATIONS("Decorations", (byte) 1),
    PARTIES("Parties", (byte) 2),
    PHOTOS_FILMING("Photos/filming", (byte) 3),
    BREAKFAST_LUNCH_DINNER("Breakfast, lunch, dinner", (byte) 4),
    SOFT_HOT_DRINK("Soft/hot drinks", (byte) 5);

    @Getter
    private final String name;

    @Getter
    private final Byte databaseCode;
}
