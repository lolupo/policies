package com.tinubu.policies.application.api;

import com.tinubu.policies.application.InsurancePolicyService;
import com.tinubu.policies.domain.InsurancePolicy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(summary = "List all policies with pagination",
            description = "Returns 200 OK with the list of policies paginated.")
    @GetMapping(produces = "application/vnd.tinubu.policies.v1+json")
    @ResponseStatus(org.springframework.http.HttpStatus.OK)
    public List<InsurancePolicyDTO> listPoliciesV1(
            @Parameter(description = "Page number (0..N)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        Page<InsurancePolicyDTO> pageResult = service.listPolicies(pageable).map(dtoMapper::toDto);
        return pageResult.getContent();
    }

    @Operation(summary = "Create a new policy",
            description = "Returns 201 Created with the created policy DTO.")
    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public InsurancePolicyDTO createPolicy(@RequestBody @Valid InsurancePolicyDTO dto) {
        InsurancePolicy created = service.createPolicy(dtoMapper.toDomain(dto));
        return dtoMapper.toDto(created);
    }

    @Operation(summary = "Get a policy by id",
            description = "Returns 200 OK with the policy DTO if found, 404 Not Found otherwise.")
    @GetMapping("/{id}")
    public ResponseEntity<InsurancePolicyDTO> getPolicy(@PathVariable Integer id) {
        InsurancePolicy policy = service.getPolicy(id);
        if (policy == null) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND).build();
        }
        InsurancePolicyDTO dto = dtoMapper.toDto(policy);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Update a policy",
            description = "Returns 200 OK with the updated policy DTO. Returns 400 Bad Request if path id and DTO id differ.")
    @PutMapping("/{id}")
    public ResponseEntity<InsurancePolicyDTO> updatePolicy(@PathVariable Integer id, @RequestBody @Valid InsurancePolicyDTO dto) {
        if (dto.getId() != null && !id.equals(dto.getId())) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).build();
        }
        InsurancePolicy updated = service.updatePolicy(id, dtoMapper.toDomain(dto));
        return ResponseEntity.ok(dtoMapper.toDto(updated));
    }
}
