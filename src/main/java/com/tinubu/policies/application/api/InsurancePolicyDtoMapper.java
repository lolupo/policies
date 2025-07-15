package com.tinubu.policies.application.api;

import com.tinubu.policies.domain.InsurancePolicy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InsurancePolicyDtoMapper {
    default InsurancePolicy toDomain(InsurancePolicyDTO dto) {
        if (dto == null) return null;
        if (dto.getId() != null && dto.getCreationDate() != null) {
            return InsurancePolicy.from(
                    dto.getId(),
                    dto.getName(),
                    dto.getStatus(),
                    dto.getCoverageStartDate(),
                    dto.getCoverageEndDate(),
                    dto.getCreationDate(),
                    dto.getUpdateDate()
            );
        } else {
            return InsurancePolicy.create(
                    dto.getName(),
                    dto.getStatus(),
                    dto.getCoverageStartDate(),
                    dto.getCoverageEndDate()
            );
        }
    }

    InsurancePolicyDTO toDto(InsurancePolicy domain);
}

