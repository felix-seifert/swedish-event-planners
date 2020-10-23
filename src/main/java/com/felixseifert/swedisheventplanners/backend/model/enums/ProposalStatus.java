package com.felixseifert.swedisheventplanners.backend.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ProposalStatus {

    INITIATED("Request initiated", 1),
    PROCESSING("Request being processed", 2),
    EXTRA_STAFF_REQUESTED("Extra staff requested", 3),
    STAFF_REQUEST_SOLVED_WITH_RECRUITMENT("Staff request solved with recruitment", 31),
    STAFF_REQUEST_SOLVED_WITH_OUTSOURCING("Staff request solved with outsourcing", 32),
    UNDER_REVIEW_BY_SUBTEAMS("Subteams revision", 4),
    EXTRA_BUDGET_REQUESTED_BY_SUBTEAM("Extra budget requested by subteam", 5),
    UNDER_REVIEW_BY_MANAGER("Manager revision", 6),
    EXTRA_BUDGET_REQUESTED("Extra budget requested", 7),
    BUDGET_REQUEST_HANDLED("Budget request handled", 71),
    CLOSED("Closed", 8);

    @Getter
    private final String status;

    @Getter
    private final Integer databaseCode;

    @Override
    public String toString() {
        return this.status;
    }
}
