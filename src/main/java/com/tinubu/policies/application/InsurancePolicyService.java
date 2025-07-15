package com.tinubu.policies.application;

import com.tinubu.policies.domain.InsurancePolicy;
import com.tinubu.policies.infrastructure.persistence.InsurancePolicyEntity;
import com.tinubu.policies.infrastructure.persistence.InsurancePolicyMapper;
import com.tinubu.policies.infrastructure.persistence.InsurancePolicyRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InsurancePolicyService {

    private final InsurancePolicyRepository repository;
    private final InsurancePolicyMapper mapper;

    @CacheEvict(value = "policies", allEntries = true)
    public InsurancePolicy createPolicy(InsurancePolicy policy) {
        InsurancePolicyEntity entity = mapper.toEntity(policy);
        InsurancePolicyEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    public InsurancePolicy getPolicy(Integer id) {
        return repository.findById(id)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Cacheable("policies")
    public Page<InsurancePolicy> listPolicies(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDomain);
    }

    @CacheEvict(value = "policies", allEntries = true)
    public InsurancePolicy updatePolicy(Integer id, InsurancePolicy updated) {
        return repository.findById(id)
                .map(existing -> {
                    InsurancePolicyEntity entity = mapper.toEntity(updated);
                    entity.setId(id);
                    // Always keep the original creationDate
                    entity.setCreationDate(existing.getCreationDate());
                    InsurancePolicyEntity saved = repository.save(entity);
                    return mapper.toDomain(saved);
                })
                .orElseThrow(() -> new IllegalArgumentException("Policy not found"));
    }
}
