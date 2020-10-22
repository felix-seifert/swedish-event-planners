package com.felixseifert.swedisheventplanners.backend.repos;

import com.felixseifert.swedisheventplanners.backend.model.Proposal;
import com.felixseifert.swedisheventplanners.backend.model.enums.ProposalStatus;
import org.hibernate.jpa.QueryHints;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProposalRepositoryCustomImpl implements ProposalRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Proposal> findAllByProposalStatusIn(Set<ProposalStatus> proposalStatusSet) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Proposal> cq = cb.createQuery(Proposal.class);
        Root<Proposal> root = cq.from(Proposal.class);

        Set<ProposalStatus> productionAndServiceProposalStatuses = proposalStatusSet.stream()
                .flatMap(this::transferToProductionOrServiceProposalStatus).collect(Collectors.toSet());

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(root.get("productionProposalStatus").in(productionAndServiceProposalStatuses));
        predicates.add(root.get("serviceProposalStatus").in(productionAndServiceProposalStatuses));

        cq.where(cb.or(predicates.toArray(new Predicate[predicates.size()])));

        return entityManager.createQuery(cq).setHint(QueryHints.HINT_READONLY, true).getResultList();
    }

    private Stream<ProposalStatus> transferToProductionOrServiceProposalStatus(ProposalStatus proposalStatus) {
        if(ProposalStatus.INITIATED.equals(proposalStatus)
                || ProposalStatus.CLOSED.equals(proposalStatus)) {
            return Stream.of(proposalStatus);
        }
        return EnumSet.complementOf(EnumSet.of(ProposalStatus.INITIATED, ProposalStatus.CLOSED)).stream();
    }
}
