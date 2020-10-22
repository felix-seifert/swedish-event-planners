package com.felixseifert.swedisheventplanners.backend.repos;

import com.felixseifert.swedisheventplanners.backend.model.Proposal;
import com.felixseifert.swedisheventplanners.backend.model.enums.ProposalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {

    Optional<Proposal> findById(Long id);

    List<Proposal> findAllByProposalStatusIn(Set<ProposalStatus> proposalStatusSet);

    List<Proposal> findAllByProductionProposalStatusIn(Set<ProposalStatus> productionProposalStatusSet);

    List<Proposal> findAllByServiceProposalStatusIn(Set<ProposalStatus> serviceProposalStatusSet);
}
