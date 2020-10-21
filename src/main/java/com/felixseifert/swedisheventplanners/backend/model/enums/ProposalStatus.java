package com.felixseifert.swedisheventplanners.backend.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ProposalStatus {

    INITIATED("Request initiated", 1),
    UNDER_REVIEW_BY_SUBTEAMS("Subteams revision", 2),
    UNDER_REVIEW_BY_MANAGER("Manager revision", 3),
    CLOSED("Closed", 4);

    @Getter
    private final String status;

    @Getter
    private final Integer databaseCode;

    @Override
    public String toString() {
        return this.status;
    }
}
