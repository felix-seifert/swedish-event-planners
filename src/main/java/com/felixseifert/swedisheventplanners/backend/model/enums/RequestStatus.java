package com.felixseifert.swedisheventplanners.backend.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RequestStatus {
    UNDER_REVIEW_BY_SCSO("SCSO revision", 1),
    UNDER_REVIEW_BY_FM("FM revision", 2),
    UNDER_REVIEW_BY_AM("AM revision", 3),
    APPROVED("Approved", 4),
    REJECTED_BY_SCSO("Rejected by SCSO", 11),
    REJECTED_BY_FM("Rejected by FM", 12),
    REJECTED_BY_AM("Rejected by AM", 13),
    REJECTION_NOTIFICATION("Closed", 20),
    MEETING_ARRANGED("Meeting arranged", 30);

    @Getter
    private final String status;

    @Getter
    private final Integer databaseCode;

    @Override
    public String toString() {
        return this.status;
    }
}
