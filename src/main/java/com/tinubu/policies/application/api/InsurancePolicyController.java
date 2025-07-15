package com.tinubu.policies.application.api;

import com.tinubu.policies.application.InsurancePolicyService;
import com.tinubu.policies.domain.InsurancePolicy;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/policies")
public class InsurancePolicyController {
    private final InsurancePolicyService service;
    private final InsurancePolicyDtoMapper dtoMapper;

    @GetMapping(produces = "application/vnd.tinubu.policies.v1+json")
    public List<InsurancePolicyDTO> listPoliciesV1(Pageable pageable) {
        Page<InsurancePolicyDTO> page = service.listPolicies(pageable).map(dtoMapper::toDto);
        return page.getContent();
    }

    @PostMapping
    public ResponseEntity<InsurancePolicyDTO> createPolicy(@RequestBody @Valid InsurancePolicyDTO dto) {
        InsurancePolicy created = service.createPolicy(dtoMapper.toDomain(dto));
        return ResponseEntity.status(201).body(dtoMapper.toDto(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsurancePolicyDTO> getPolicy(@PathVariable Integer id) {
        InsurancePolicy policy = service.getPolicy(id);
        if (policy == null) {
            return ResponseEntity.notFound().build();
        }
        InsurancePolicyDTO dto = dtoMapper.toDto(policy);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InsurancePolicyDTO> updatePolicy(@PathVariable Integer id, @RequestBody @Valid InsurancePolicyDTO dto) {
        if (dto.getId() != null && !id.equals(dto.getId())) {
            throw new IllegalArgumentException("Path id and DTO id must be identical");
        }
        InsurancePolicy updated = service.updatePolicy(id, dtoMapper.toDomain(dto));
        return ResponseEntity.ok(dtoMapper.toDto(updated));
    }
}
