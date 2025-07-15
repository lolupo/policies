package com.tinubu.policies.infrastructure.persistence;

import com.tinubu.policies.domain.InsurancePolicy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InsurancePolicyMapper {
    default InsurancePolicy toDomain(InsurancePolicyEntity entity) {
        if (entity == null) return null;
        return InsurancePolicy.from(
                entity.getId(),
                entity.getName(),
                entity.getStatus(),
                entity.getCoverageStartDate(),
                entity.getCoverageEndDate(),
                entity.getCreationDate(),
                entity.getUpdateDate());
    }

    InsurancePolicyEntity toEntity(InsurancePolicy domain);
}
