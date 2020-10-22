package com.felixseifert.swedisheventplanners.backend.service;

import com.felixseifert.swedisheventplanners.backend.model.Proposal;
import com.felixseifert.swedisheventplanners.backend.model.enums.ProposalStatus;

import java.util.List;

public interface ProposalService {

    List<Proposal> getAllProposals();

    List<Proposal> getAllProposalsByStatus(ProposalStatus proposalStatus);

    List<Proposal> getAllProposalsByProductionStatus(ProposalStatus productionProposalStatus);

    List<Proposal> getAllProposalsByServiceStatus(ProposalStatus serviceProposalStatus);

    Proposal getProposalById(Long id);

    Proposal postProposal(Proposal proposal);

    Proposal putProposal(Proposal proposal);

    void deleteProposal(Proposal proposal);
}
