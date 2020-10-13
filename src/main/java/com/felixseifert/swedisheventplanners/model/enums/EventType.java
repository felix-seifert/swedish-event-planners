package com.felixseifert.swedisheventplanners.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EventType {
    CELEBRATION("Celebration", (byte) 1),
    CONFERENCE("Conference", (byte) 2),
    SUMMER_SCHOOL("Summer School", (byte) 3),
    TEAM_BUILDING("Team Building", (byte) 4),
    WORKSHOP("Workshop", (byte) 5),
    OTHER("Other", (byte) 60);

    @Getter
    private final String name;

    @Getter
    private final Byte databaseCode;
}
