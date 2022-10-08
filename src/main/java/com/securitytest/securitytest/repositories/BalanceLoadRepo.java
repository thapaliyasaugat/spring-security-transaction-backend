package com.securitytest.securitytest.repositories;

import com.securitytest.securitytest.models.BalanceLoadDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceLoadRepo extends JpaRepository<BalanceLoadDetail,Integer> {
    Page<BalanceLoadDetail> findAllByLoadedBy(String email, Pageable pageable);
    Page<BalanceLoadDetail> findAll(Pageable pageable);
}
