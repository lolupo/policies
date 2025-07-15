package com.tinubu.policies.application;

import com.tinubu.policies.domain.InsurancePolicy;
import com.tinubu.policies.domain.PolicyStatus;
import com.tinubu.policies.infrastructure.persistence.InsurancePolicyEntity;
import com.tinubu.policies.infrastructure.persistence.InsurancePolicyMapper;
import com.tinubu.policies.infrastructure.persistence.InsurancePolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class InsurancePolicyServiceTest {
    @Mock
    private InsurancePolicyRepository repository;
    @Mock
    private InsurancePolicyMapper mapper;

    @InjectMocks
    private InsurancePolicyService service;

    private InsurancePolicyEntity entity;
    private InsurancePolicy domain;

    @BeforeEach
    void setUp() {
        entity = new InsurancePolicyEntity(
                1,
                "Test Policy",
                PolicyStatus.ACTIVE,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 1)
        );
        domain = InsurancePolicy.from(
                entity.getId(),
                entity.getName(),
                entity.getStatus(),
                entity.getCoverageStartDate(),
                entity.getCoverageEndDate(),
                entity.getCreationDate(),
                entity.getUpdateDate()
        );
    }

    @Test
    void shouldCreatePolicy() {
        Mockito.when(mapper.toEntity(domain)).thenReturn(entity);
        Mockito.when(repository.save(entity)).thenReturn(entity);
        Mockito.when(mapper.toDomain(entity)).thenReturn(domain);
        InsurancePolicy created = service.createPolicy(domain);
        assertNotNull(created.getId());
        assertEquals("Test Policy", created.getName());
    }

    @Test
    void shouldFindPolicyById() {
        Mockito.when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(mapper.toDomain(entity)).thenReturn(domain);
        InsurancePolicy found = service.getPolicy(entity.getId());
        assertNotNull(found);
        assertEquals(entity.getId(), found.getId());
    }

    @Test
    void shouldReturnPaginatedPolicies() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<InsurancePolicyEntity> page = new PageImpl<>(List.of(entity), pageable, 1);
        Mockito.when(repository.findAll(pageable)).thenReturn(page);
        Mockito.when(mapper.toDomain(entity)).thenReturn(domain);
        Page<InsurancePolicy> result = service.listPolicies(pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(entity.getId(), result.getContent().getFirst().getId());
    }
}
