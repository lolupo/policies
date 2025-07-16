package com.tinubu.policies.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicyEntity, Integer> {
    Optional<InsurancePolicyEntity> findByIdAndExpiryDateIsNull(Integer id);

    Page<InsurancePolicyEntity> findAllByExpiryDateIsNull(Pageable pageable);
}
