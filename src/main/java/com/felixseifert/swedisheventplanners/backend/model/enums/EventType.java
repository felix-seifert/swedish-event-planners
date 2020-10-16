package com.felixseifert.swedisheventplanners.backend.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EventType {
    CELEBRATION("Celebration", 1),
    CONFERENCE("Conference", 2),
    SUMMER_SCHOOL("Summer School", 3),
    TEAM_BUILDING("Team Building", 4),
    WORKSHOP("Workshop", 5),
    OTHER("Other", 60);

    @Getter
    private final String name;

    @Getter
    private final Integer databaseCode;
}
