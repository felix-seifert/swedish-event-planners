package com.felixseifert.swedisheventplanners.backend.service;

import com.felixseifert.swedisheventplanners.backend.model.NewRequest;

import java.util.List;

public interface NewRequestService {

    List<NewRequest> getAllNewRequests();

    NewRequest getNewRequestById(Long id);

    NewRequest postNewRequest(NewRequest newRequest);

    NewRequest putNewRequest(NewRequest newRequest);

    void deleteNewRequest(NewRequest newRequest);
}
