package com.felixseifert.swedisheventplanners.backend.repos;

import com.felixseifert.swedisheventplanners.backend.model.Proposal;
import com.felixseifert.swedisheventplanners.backend.model.enums.ProposalStatus;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;

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

@Repository
public class ProposalRepositoryCustomImpl implements ProposalRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Proposal> findAllByProposalStatusIn(Set<ProposalStatus> proposalStatusSet) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Proposal> cq = cb.createQuery(Proposal.class);
        Root<Proposal> root = cq.from(Proposal.class);

        List<Predicate> predicates = new ArrayList<>();
        String pStatus = "productionProposalStatus";
        String sStatus = "serviceProposalStatus";

        // Add predicates according to how transient attribute proposalStatus in class Proposal is defined.
        // givenProposalStatusSet is looped.
        // * If status is INITIATED, pStatus and sStatus have to be INITIATED
        // * If status is CLOSED, pStatus and sStatus have to be closed
        // * In all other cases, either pStatus or sStatus have to be of any status which is not CLOSED or INITIATED
        // Eventually, those predicates are combined with 'or' to reflect initial given proposalStatusSet.
        for(ProposalStatus status : proposalStatusSet) {
            if(ProposalStatus.INITIATED.equals(status)) {
                predicates.add(cb.and(
                        cb.equal(root.get(pStatus), ProposalStatus.INITIATED),
                        cb.equal(root.get(sStatus), ProposalStatus.INITIATED)
                ));
                continue;
            }
            if(ProposalStatus.CLOSED.equals(status)) {
                predicates.add(cb.and(
                        cb.equal(root.get(pStatus), ProposalStatus.CLOSED),
                        cb.equal(root.get(sStatus), ProposalStatus.CLOSED)
                ));
                continue;
            }
            predicates.add(cb.or(
                    cb.not(root.get(pStatus).in(EnumSet.of(ProposalStatus.INITIATED, ProposalStatus.CLOSED))),
                    cb.not(root.get(sStatus).in(EnumSet.of(ProposalStatus.INITIATED, ProposalStatus.CLOSED)))
            ));
        }

        cq.where(cb.or(predicates.toArray(new Predicate[predicates.size()])));

        return entityManager.createQuery(cq).setHint(QueryHints.HINT_READONLY, true).getResultList();
    }
}
