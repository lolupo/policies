package com.tinubu.policies.infrastructure.persistence;

import com.tinubu.policies.domain.PolicyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "insurance_policy")
public class InsurancePolicyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PolicyStatus status;

    @Column(name = "coverage_start_date", nullable = false)
    private LocalDate coverageStartDate;

    @Column(name = "coverage_end_date", nullable = false)
    private LocalDate coverageEndDate;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDate creationDate;

    @Setter
    @Column(name = "update_date", nullable = false)
    private LocalDate updateDate;

    @PrePersist
    protected void onCreate() {
        LocalDate now = LocalDate.now();
        if (creationDate == null) creationDate = now;
        if (updateDate == null) updateDate = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = LocalDate.now();
    }

}
