package com.felixseifert.swedisheventplanners.backend.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RequestStatus {
    UNDER_REVIEW_BY_SCSO("Senior Customer Service Officer revision", 1),
    UNDER_REVIEW_BY_FM("Financial Manager revision", 2),
    UNDER_REVIEW_BY_AM("Administration Manager revision", 3),
    APPROVED("Approved", 4);

    @Getter
    private final String status;

    @Getter
    private final Integer databaseCode;

    @Override
    public String toString() {
        return this.status;
    }
}
