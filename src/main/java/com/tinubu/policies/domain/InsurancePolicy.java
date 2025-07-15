package com.tinubu.policies.domain;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class InsurancePolicy {
    private final Integer id;
    private final String name;
    private final PolicyStatus status;
    private final LocalDate coverageStartDate;
    private final LocalDate coverageEndDate;
    private final LocalDate creationDate;
    private final LocalDate updateDate;

    private InsurancePolicy(Integer id, String name, PolicyStatus status, LocalDate coverageStartDate, LocalDate coverageEndDate, LocalDate creationDate, LocalDate updateDate) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.coverageStartDate = coverageStartDate;
        this.coverageEndDate = coverageEndDate;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
    }

    public static InsurancePolicy create(String name, PolicyStatus status, LocalDate coverageStartDate, LocalDate coverageEndDate) {
        validate(name, status, coverageStartDate, coverageEndDate);
        //Id and Dates are managed by persistence
        return new InsurancePolicy(null, name, status, coverageStartDate, coverageEndDate, null, null);
    }

    public static InsurancePolicy from(Integer id, String name, PolicyStatus status, LocalDate coverageStartDate, LocalDate coverageEndDate, LocalDate creationDate, LocalDate updateDate) {
        validate(name, status, coverageStartDate, coverageEndDate);
        return new InsurancePolicy(id, name, status, coverageStartDate, coverageEndDate, creationDate, updateDate);
    }

    private static void validate(String name, PolicyStatus status, LocalDate coverageStartDate, LocalDate coverageEndDate) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Policy name must not be null or empty");
        }
        if (status == null) {
            throw new IllegalArgumentException("Policy status must not be null");
        }
        if (coverageStartDate == null || coverageEndDate == null) {
            throw new IllegalArgumentException("Coverage dates must not be null");
        }
    }

}
