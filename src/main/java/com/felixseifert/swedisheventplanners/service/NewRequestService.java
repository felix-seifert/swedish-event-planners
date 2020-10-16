package com.felixseifert.swedisheventplanners.service;

import com.felixseifert.swedisheventplanners.model.NewRequest;

import java.util.List;

public interface NewRequestService {

    List<NewRequest> getAllNewRequests();

    NewRequest getNewRequestById(Long id);

    NewRequest postNewRequest(NewRequest newRequest);

    NewRequest putNewRequest(NewRequest newRequest);

    void deleteNewRequest(NewRequest newRequest);
}
