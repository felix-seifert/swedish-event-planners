package com.felixseifert.swedisheventplanners.backend.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ProposalStatus {

    INITIATED("Request initiated", 1),
    PROCESSING("Request being processed", 2),
    EXTRA_STAFF_REQUESTED("Extra staff requested", 3),
    UNDER_REVIEW_BY_SUBTEAMS("Subteams revision", 4),
    UNDER_REVIEW_BY_MANAGER("Manager revision", 5),
    EXTRA_BUDGET_REQUESTED("Extra budget requested", 6),
    CLOSED("Closed", 7);

    @Getter
    private final String status;

    @Getter
    private final Integer databaseCode;

    @Override
    public String toString() {
        return this.status;
    }
}