package com.securitytest.securitytest.repositories;

import com.securitytest.securitytest.models.CashbackScheme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashbackRepo extends JpaRepository<CashbackScheme,Integer> {
    Page<CashbackScheme> findByInitiatedBy(Pageable pageable,String email);
}
