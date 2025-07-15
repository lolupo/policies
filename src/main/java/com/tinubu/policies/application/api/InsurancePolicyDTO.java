package com.tinubu.policies.application.api;

import com.tinubu.policies.domain.PolicyStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class InsurancePolicyDTO {
    private Integer id;
    @NotNull(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    @NotNull(message = "Status is required")
    private PolicyStatus status;
    @NotNull(message = "Coverage start date is required")
    private LocalDate coverageStartDate;
    @NotNull(message = "Coverage end date is required")
    private LocalDate coverageEndDate;
    private LocalDate creationDate;
    private LocalDate updateDate;
}
