package com.felixseifert.swedisheventplanners.backend.service;

import com.felixseifert.swedisheventplanners.backend.model.NewRequest;
import com.felixseifert.swedisheventplanners.backend.model.enums.RequestStatus;

import java.util.List;
import java.util.Set;

public interface NewRequestService {

    List<NewRequest> getAllNewRequests();

    List<NewRequest> getAllNewRequestsByStatus(Set<RequestStatus> requestStatusSet);

    NewRequest getNewRequestById(Long id);

    NewRequest postNewRequest(NewRequest newRequest);

    NewRequest putNewRequest(NewRequest newRequest);

    void deleteNewRequest(NewRequest newRequest);
}
