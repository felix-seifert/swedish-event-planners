package com.felixseifert.swedisheventplanners.repos;

import com.felixseifert.swedisheventplanners.model.NewRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewRequestRepository extends JpaRepository<NewRequest, Long> {

    Optional<NewRequest> findById(Long id);
}
