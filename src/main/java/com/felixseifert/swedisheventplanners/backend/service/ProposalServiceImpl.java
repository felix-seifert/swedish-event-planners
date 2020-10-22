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
    public List<Proposal> getAllProposalsByStatus(ProposalStatus proposalStatus) {
        List<Proposal> proposalsByStatus = proposalRepository.findAllByProposalStatus(proposalStatus);
        log.debug("Return {} proposals with status {}", proposalsByStatus.size(), proposalStatus);
        return proposalsByStatus;
    }

    @Override
    public List<Proposal> getAllProposalsByProductionStatus(ProposalStatus productionProposalStatus) {
        List<Proposal> proposalsByStatus = proposalRepository.findAllByProductionProposalStatus(productionProposalStatus);
        log.debug("Return {} proposals with status {}", proposalsByStatus.size(), productionProposalStatus);
        return proposalsByStatus;
    }

    @Override
    public List<Proposal> getAllProposalsByServiceStatus(ProposalStatus serviceProposalStatus) {
        List<Proposal> proposalsByStatus = proposalRepository.findAllByServiceProposalStatus(serviceProposalStatus);
        log.debug("Return {} proposals with status {}", proposalsByStatus.size(), serviceProposalStatus);
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
