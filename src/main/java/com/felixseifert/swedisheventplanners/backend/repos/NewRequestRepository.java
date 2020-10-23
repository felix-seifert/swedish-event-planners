package com.felixseifert.swedisheventplanners.backend.repos;

import com.felixseifert.swedisheventplanners.backend.model.NewRequest;
import com.felixseifert.swedisheventplanners.backend.model.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface NewRequestRepository extends JpaRepository<NewRequest, Long> {

    Optional<NewRequest> findById(Long id);

    List<NewRequest> findByRequestStatusIn(Set<RequestStatus> requestStatusSet);
}
