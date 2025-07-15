package com.tinubu.policies.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InsurancePolicyTest {
    @Test
    void shouldCreateValidInsurancePolicy() {
        InsurancePolicy policy = InsurancePolicy.create(
                "Professional Liability",
                PolicyStatus.ACTIVE,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31)
        );
        assertNull(policy.getId());
        assertEquals("Professional Liability", policy.getName());
        assertEquals(PolicyStatus.ACTIVE, policy.getStatus());
        assertEquals(LocalDate.of(2025, 1, 1), policy.getCoverageStartDate());
        assertEquals(LocalDate.of(2025, 12, 31), policy.getCoverageEndDate());
        assertNull(policy.getCreationDate());
        assertNull(policy.getUpdateDate());
    }

    @Test
    void shouldThrowExceptionForEmptyName() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(1);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> InsurancePolicy.create("", PolicyStatus.ACTIVE, start, end));
        assertTrue(exception.getMessage().contains("Policy name must not be null or empty"));
    }

    @Test
    void shouldThrowExceptionForNullStatus() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(1);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> InsurancePolicy.create("Test", null, start, end));
        assertTrue(exception.getMessage().contains("Policy status must not be null"));
    }

    @Test
    void shouldThrowExceptionForNullCoverageDates() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> InsurancePolicy.create("Test", PolicyStatus.ACTIVE, null, null));
        assertTrue(exception.getMessage().contains("Coverage dates must not be null"));
    }
}
