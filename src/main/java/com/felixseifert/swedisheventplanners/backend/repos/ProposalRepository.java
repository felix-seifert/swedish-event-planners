package com.felixseifert.swedisheventplanners.backend.repos;

import com.felixseifert.swedisheventplanners.backend.model.Proposal;
import com.felixseifert.swedisheventplanners.backend.model.enums.ProposalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {

    Optional<Proposal> findById(Long id);

    List<Proposal> findAllByProposalStatus(ProposalStatus proposalStatus);

    List<Proposal> findAllByProductionProposalStatus(ProposalStatus productionProposalStatus);

    List<Proposal> findAllByServiceProposalStatus(ProposalStatus serviceProposalStatus);
}
