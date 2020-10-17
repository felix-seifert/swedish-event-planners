package com.felixseifert.swedisheventplanners.backend.service;

import com.felixseifert.swedisheventplanners.backend.model.NewRequest;
import com.felixseifert.swedisheventplanners.backend.model.enums.RequestStatus;

import java.util.List;

public interface NewRequestService {

    List<NewRequest> getAllNewRequests();

    List<NewRequest> getAllNewRequestsByStatus(RequestStatus requestStatus);

    NewRequest getNewRequestById(Long id);

    NewRequest postNewRequest(NewRequest newRequest);

    NewRequest putNewRequest(NewRequest newRequest);

    void deleteNewRequest(NewRequest newRequest);
}
