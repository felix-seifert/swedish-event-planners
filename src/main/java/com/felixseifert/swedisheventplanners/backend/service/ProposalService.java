package com.felixseifert.swedisheventplanners.backend.service;

import com.felixseifert.swedisheventplanners.backend.model.Proposal;
import com.felixseifert.swedisheventplanners.backend.model.enums.ProposalStatus;

import java.util.List;
import java.util.Set;

public interface ProposalService {

    List<Proposal> getAllProposals();

    List<Proposal> getAllProposalsByStatus(Set<ProposalStatus> proposalStatus);

    List<Proposal> getAllProposalsByProductionStatus(Set<ProposalStatus> productionProposalStatus);

    List<Proposal> getAllProposalsByServiceStatus(Set<ProposalStatus> serviceProposalStatus);

    Proposal getProposalById(Long id);

    Proposal postProposal(Proposal proposal);

    Proposal putProposal(Proposal proposal);

    void deleteProposal(Proposal proposal);
}
