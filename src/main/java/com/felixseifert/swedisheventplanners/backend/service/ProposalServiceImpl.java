package com.felixseifert.swedisheventplanners.backend.service;

import com.felixseifert.swedisheventplanners.backend.exceptions.BlankValueNotAllowedException;
import com.felixseifert.swedisheventplanners.backend.exceptions.EntityAlreadyExistsException;
import com.felixseifert.swedisheventplanners.backend.exceptions.ValueNotAllowedException;
import com.felixseifert.swedisheventplanners.backend.model.Proposal;
import com.felixseifert.swedisheventplanners.backend.model.enums.ProposalStatus;
import com.felixseifert.swedisheventplanners.backend.repos.ProposalRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class ProposalServiceImpl implements ProposalService {

    @Autowired
    ProposalRepository proposalRepository;

    @Override
    public List<Proposal> getAllProposals() {
        List<Proposal> proposals = proposalRepository.findAll();
        log.debug("Return {} proposals", proposals.size());
        return proposals;
    }

    @Override
    public List<Proposal> getAllProposalsByStatus(Set<ProposalStatus> proposalStatusSet) {
        List<Proposal> proposalsByStatus = proposalRepository.findAllByProposalStatusIn(proposalStatusSet);
        log.debug("Return {} proposals with status {}", proposalsByStatus.size(), proposalStatusSet);
        return proposalsByStatus;
    }

    @Override
    public List<Proposal> getAllProposalsByProductionStatus(Set<ProposalStatus> productionProposalStatusSet) {
        List<Proposal> proposalsByStatus = proposalRepository.findAllByProductionProposalStatusIn(productionProposalStatusSet);
        log.debug("Return {} proposals with status {}", proposalsByStatus.size(), productionProposalStatusSet);
        return proposalsByStatus;
    }

    @Override
    public List<Proposal> getAllProposalsByServiceStatus(Set<ProposalStatus> serviceProposalStatusSet) {
        List<Proposal> proposalsByStatus = proposalRepository.findAllByServiceProposalStatusIn(serviceProposalStatusSet);
        log.debug("Return {} proposals with status {}", proposalsByStatus.size(), serviceProposalStatusSet);
        return proposalsByStatus;
    }

    @Override
    public Proposal getProposalById(Long id) {
        Optional<Proposal> proposal = proposalRepository.findById(id);
        if(proposal.isEmpty()) {
            throw new EntityNotFoundException("Proposal with id " + id + " does not exist");
        }
        log.debug("Return newRequest {}", proposal.get());
        return proposal.get();
    }

    @Override
    public Proposal postProposal(Proposal proposal) {
        if(proposal.getId() != null) {
            throw new EntityAlreadyExistsException("Given Proposal already has id");
        }

        checkIllegalVariables(proposal);

        Proposal newProposal = proposalRepository.save(proposal);
        log.info("Proposal {} created", newProposal);
        return proposal;
    }

    @Override
    public Proposal putProposal(Proposal proposal) {
        if(proposal.getId() == null) {
            throw new EntityNotFoundException("Proposal does not have id");
        }

        checkIllegalVariables(proposal);

        Proposal newProposal = proposalRepository.save(proposal);
        log.info("Proposal {} update", newProposal);
        return newProposal;
    }

    @Override
    public void deleteProposal(Proposal proposal) {
        if(proposal.getId() == null) {
            throw new EntityNotFoundException("Proposal does not have id");
        }

        proposalRepository.delete(proposal);
        log.info("Proposal {} deleted", proposal);
    }

    private void checkIllegalVariables(Proposal proposal) {

        if(StringUtils.isBlank(proposal.getRecordNumber())) {
            throw new BlankValueNotAllowedException("NewRequest's recordNumber is blank");
        }
        if(proposal.getClient() == null) {
            throw new BlankValueNotAllowedException("NewRequest does not have a Client");
        }
        if(proposal.getEventType() == null) {
            throw new BlankValueNotAllowedException("NewRequest does not have an EventType");
        }
        if(proposal.getFrom() == null) {
            throw new BlankValueNotAllowedException("NewRequest does not have a start DateTime");
        }
        if(proposal.getTo() == null) {
            throw new BlankValueNotAllowedException("NewRequest does not have an end DateTime");
        }

        if(proposal.getRecordNumber().length() != 10) {
            throw new ValueNotAllowedException("NewRequest's recordNumber is not ten digits long");
        }
        if(!proposal.getTo().isAfter(proposal.getFrom())) {
            throw new ValueNotAllowedException("NewRequest's start data cannot be after its end date");
        }
    }
}
