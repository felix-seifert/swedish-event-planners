package com.felixseifert.swedisheventplanners.backend.repos;

import com.felixseifert.swedisheventplanners.backend.model.Proposal;
import com.felixseifert.swedisheventplanners.backend.model.enums.ProposalStatus;

import java.util.List;
import java.util.Set;

public interface ProposalRepositoryCustom {

    List<Proposal> findAllByProposalStatusIn(Set<ProposalStatus> proposalStatusSet);
}
