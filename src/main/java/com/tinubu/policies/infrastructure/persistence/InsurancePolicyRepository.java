package com.tinubu.policies.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicyEntity, Integer> {
}
