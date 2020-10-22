package com.felixseifert.swedisheventplanners.backend.service;

import com.felixseifert.swedisheventplanners.backend.exceptions.BlankValueNotAllowedException;
import com.felixseifert.swedisheventplanners.backend.exceptions.EntityAlreadyExistsException;
import com.felixseifert.swedisheventplanners.backend.exceptions.ValueNotAllowedException;
import com.felixseifert.swedisheventplanners.backend.model.NewRequest;
import com.felixseifert.swedisheventplanners.backend.model.enums.RequestStatus;
import com.felixseifert.swedisheventplanners.backend.repos.NewRequestRepository;
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
public class NewRequestServiceImpl implements NewRequestService {

    @Autowired
    private NewRequestRepository newRequestRepository;

    @Override
    public List<NewRequest> getAllNewRequests() {
        List<NewRequest> newRequests = newRequestRepository.findAll();
        log.debug("Return {} new requests", newRequests.size());
        return newRequests;
    }

    @Override
    public List<NewRequest> getAllNewRequestsByStatus(Set<RequestStatus> requestStatusSet) {
        List<NewRequest> newRequestsByStatus = newRequestRepository.findByRequestStatusIn(requestStatusSet);
        log.debug("Return {} new requests with status in {}", newRequestsByStatus.size(), requestStatusSet);
        return newRequestsByStatus;
    }

    @Override
    public NewRequest getNewRequestById(Long id) {
        Optional<NewRequest> newRequest = newRequestRepository.findById(id);
        if(newRequest.isEmpty()) {
            throw new EntityNotFoundException("NewRequest with id " + id + " does not exist");
        }
        log.debug("Return newRequest {}", newRequest.get());
        return newRequest.get();
    }

    @Override
    public NewRequest postNewRequest(NewRequest newRequest) {
        if(newRequest.getId() != null) {
            throw new EntityAlreadyExistsException("Given NewRequest already has id");
        }

        checkIllegalVariables(newRequest);

        NewRequest newNewRequest = newRequestRepository.save(newRequest);
        log.info("New NewRequest {} created", newNewRequest);
        return newNewRequest;
    }

    @Override
    public NewRequest putNewRequest(NewRequest newRequest) {
        if(newRequest.getId() == null) {
            throw new EntityNotFoundException("NewRequest does not have id");
        }

        checkIllegalVariables(newRequest);

        NewRequest newNewRequest = newRequestRepository.save(newRequest);
        log.info("New NewRequest {} update", newNewRequest);
        return newNewRequest;
    }

    @Override
    public void deleteNewRequest(NewRequest newRequest) {
        if(newRequest.getId() == null) {
            throw new EntityNotFoundException("NewRequest does not have id");
        }

        newRequestRepository.delete(newRequest);
        log.info("NewRequest {} deleted", newRequest);
    }

    private void checkIllegalVariables(NewRequest newRequest) {

        if(StringUtils.isBlank(newRequest.getRecordNumber())) {
            throw new BlankValueNotAllowedException("NewRequest's recordNumber is blank");
        }
        if(newRequest.getClient() == null) {
            throw new BlankValueNotAllowedException("NewRequest does not have a Client");
        }
        if(newRequest.getEventType() == null) {
            throw new BlankValueNotAllowedException("NewRequest does not have an EventType");
        }
        if(newRequest.getFrom() == null) {
            throw new BlankValueNotAllowedException("NewRequest does not have a start DateTime");
        }
        if(newRequest.getTo() == null) {
            throw new BlankValueNotAllowedException("NewRequest does not have an end DateTime");
        }

        if(newRequest.getRecordNumber().length() != 10) {
            throw new ValueNotAllowedException("NewRequest's recordNumber is not ten digits long");
        }
        if(!newRequest.getTo().isAfter(newRequest.getFrom())) {
            throw new ValueNotAllowedException("NewRequest's start data cannot be after its end date");
        }
    }
}
