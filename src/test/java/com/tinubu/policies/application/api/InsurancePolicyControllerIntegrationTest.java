package com.tinubu.policies.application.api;

import com.tinubu.policies.domain.PolicyStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class InsurancePolicyControllerIntegrationTest {

    @Container
    //Testcontainers manages the container lifecycle automatically for JUnit tests annotated with @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.2")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void overrideProps(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shouldListPolicies() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/vnd.tinubu.policies.v1+json");
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/policies",
                HttpMethod.GET,
                entity,
                String.class
        );
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("\"id\"");
        // The response is now a JSON array of DTOs, not a paged object with 'content'.
        assertThat(response.getBody().trim()).startsWith("[");
    }

    @Test
    void shouldPaginatePolicies() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/vnd.tinubu.policies.v1+json");
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/policies?page=1&size=5",
                HttpMethod.GET,
                entity,
                String.class
        );
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        // The response is a JSON array, so we check for array structure and count elements
        String body = response.getBody();
        assertThat(body.trim()).startsWith("[");
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("\\{\\s*\\\"id\\\":").matcher(body);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        assertThat(count).isEqualTo(5);
    }

    @Test
    void shouldCreatePolicy() {
        // Use the all-args constructor for DTO creation
        var dto = new InsurancePolicyDTO(null, "Test Policy", PolicyStatus.ACTIVE, LocalDate.now(), LocalDate.now().plusYears(1), LocalDate.now(), LocalDate.now());
        var response = restTemplate.postForEntity("http://localhost:" + port + "/api/policies", dto, InsurancePolicyDTO.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Test Policy");
    }

    @Test
    void shouldGetPolicyById() {
        var dto2 = new InsurancePolicyDTO(null, "GetById Policy", PolicyStatus.ACTIVE, LocalDate.now(), LocalDate.now().plusYears(1), LocalDate.now(), LocalDate.now());
        var createResponse = restTemplate.postForEntity("http://localhost:" + port + "/api/policies", dto2, InsurancePolicyDTO.class);
        Assertions.assertNotNull(createResponse.getBody());
        Integer id = createResponse.getBody().getId();
        var getResponse = restTemplate.getForEntity("http://localhost:" + port + "/api/policies/" + id, InsurancePolicyDTO.class);
        assertThat(getResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getId()).isEqualTo(id);
        assertThat(getResponse.getBody().getName()).isEqualTo("GetById Policy");
    }

    @Test
    void shouldReturnNotFoundForUnknownPolicy() {
        var response = restTemplate.getForEntity("http://localhost:" + port + "/api/policies/999999", InsurancePolicyDTO.class);
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void shouldUpdatePolicy() {
        var dto3 = new InsurancePolicyDTO(null, "Update Policy", PolicyStatus.ACTIVE, LocalDate.now(), LocalDate.now().plusYears(1), LocalDate.now(), LocalDate.now());
        var createResponse2 = restTemplate.postForEntity("http://localhost:" + port + "/api/policies", dto3, InsurancePolicyDTO.class);
        Assertions.assertNotNull(createResponse2.getBody());
        Integer id2 = createResponse2.getBody().getId();
        var updatedDto = new InsurancePolicyDTO(id2, "Update Policy", PolicyStatus.INACTIVE, LocalDate.now(), LocalDate.now().plusYears(1), LocalDate.now(), LocalDate.now());
        restTemplate.put("http://localhost:" + port + "/api/policies/" + id2, updatedDto);
        var getResponse2 = restTemplate.getForEntity("http://localhost:" + port + "/api/policies/" + id2, InsurancePolicyDTO.class);
        Assertions.assertNotNull(getResponse2.getBody());
        assertThat(getResponse2.getBody().getStatus()).isEqualTo(PolicyStatus.INACTIVE);
    }

    @Test
    void shouldNotUpdateCreationDateOnPolicyUpdate() {
        // Create a policy
        var dto = new InsurancePolicyDTO(null, "Test CreationDate", PolicyStatus.ACTIVE, LocalDate.now(), LocalDate.now().plusYears(1), LocalDate.now(), LocalDate.now());
        var createResponse = restTemplate.postForEntity("http://localhost:" + port + "/api/policies", dto, InsurancePolicyDTO.class);
        assertThat(createResponse.getStatusCode().is2xxSuccessful()).isTrue();
        var createdPolicy = createResponse.getBody();
        assertThat(createdPolicy).isNotNull();
        var originalCreationDate = createdPolicy.getCreationDate();
        var id = createdPolicy.getId();

        // Try to update with a different creationDate
        var updateDto = new InsurancePolicyDTO(id, "Test CreationDate Updated", PolicyStatus.ACTIVE, LocalDate.now(), LocalDate.now().plusYears(1), originalCreationDate.plusDays(10), LocalDate.now());
        restTemplate.put("http://localhost:" + port + "/api/policies/" + id, updateDto);

        // Retrieve and check creationDate is unchanged
        var getResponse = restTemplate.getForEntity("http://localhost:" + port + "/api/policies/" + id, InsurancePolicyDTO.class);
        assertThat(getResponse.getStatusCode().is2xxSuccessful()).isTrue();
        var updatedPolicy = getResponse.getBody();
        assertThat(updatedPolicy).isNotNull();
        assertThat(updatedPolicy.getCreationDate()).isEqualTo(originalCreationDate);
    }

    @Test
    void shouldReturnValidationErrorInProblemJsonFormat() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/vnd.tinubu.policies.v1+json");
        headers.add("Content-Type", "application/json");
        // Invalid DTO: name is null, status is null
        var invalidDto = new InsurancePolicyDTO(null, null, null, LocalDate.now(), LocalDate.now().plusYears(1), null, null);
        HttpEntity<InsurancePolicyDTO> entity = new HttpEntity<>(invalidDto, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/policies",
                HttpMethod.POST,
                entity,
                String.class
        );
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getHeaders().getContentType()).hasToString("application/problem+json");
        assertThat(response.getBody()).contains("type");
        assertThat(response.getBody()).contains("title");
        assertThat(response.getBody()).contains("status");
        assertThat(response.getBody()).contains("detail");
        assertThat(response.getBody()).contains("instance");
        assertThat(response.getBody()).contains("Validation Failed");
        assertThat(response.getBody()).contains("Name is required");
        assertThat(response.getBody()).contains("Status is required");
    }

    @Test
    void shouldReturnBadRequestInProblemJsonFormatForIllegalArgument() {
        // This test verifies that an IllegalArgumentException is handled and returns a 400 Bad Request in RFC 7807 problem+json format.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/vnd.tinubu.policies.v1+json");
        headers.add("Content-Type", "application/json");
        // Use a negative id to trigger IllegalArgumentException in the service/controller
        var invalidDto = new InsurancePolicyDTO(-999, "Test", PolicyStatus.ACTIVE, LocalDate.now(), LocalDate.now().plusYears(1), null, null);
        HttpEntity<InsurancePolicyDTO> entity = new HttpEntity<>(invalidDto, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/policies/-999",
                HttpMethod.PUT,
                entity,
                String.class
        );
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getHeaders().getContentType()).hasToString("application/problem+json");
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("type");
        assertThat(response.getBody()).contains("title");
        assertThat(response.getBody()).contains("status");
        assertThat(response.getBody()).contains("detail");
        assertThat(response.getBody()).contains("instance");
        assertThat(response.getBody()).contains("Bad Request");
    }

    @Test
    void shouldReturnValidationErrorInProblemJsonFormatForInvalidUpdate() {
        // This test verifies that a validation error (e.g. missing required field) returns a 400 Bad Request in RFC 7807 problem+json format.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/vnd.tinubu.policies.v1+json");
        headers.add("Content-Type", "application/json");
        // Use a valid id but send a DTO with a null name to trigger validation error
        var invalidDto = new InsurancePolicyDTO(1, null, PolicyStatus.ACTIVE, LocalDate.now(), LocalDate.now().plusYears(1), null, null);
        HttpEntity<InsurancePolicyDTO> entity = new HttpEntity<>(invalidDto, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/policies/1",
                HttpMethod.PUT,
                entity,
                String.class
        );
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getHeaders().getContentType()).hasToString("application/problem+json");
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("type");
        assertThat(response.getBody()).contains("title");
        assertThat(response.getBody()).contains("status");
        assertThat(response.getBody()).contains("detail");
        assertThat(response.getBody()).contains("instance");
        assertThat(response.getBody()).contains("Validation Failed");
    }

    @Test
    void shouldReturnBadRequestWhenPathIdDiffersFromDtoId() {
        // Create a policy to get a valid id
        var dto = new InsurancePolicyDTO(null, "Test Path/DTO Id", PolicyStatus.ACTIVE, LocalDate.now(), LocalDate.now().plusYears(1), LocalDate.now(), LocalDate.now());
        var createResponse = restTemplate.postForEntity("http://localhost:" + port + "/api/policies", dto, InsurancePolicyDTO.class);
        assertThat(createResponse.getStatusCode().is2xxSuccessful()).isTrue();
        var createdPolicy = createResponse.getBody();
        assertThat(createdPolicy).isNotNull();
        var validId = createdPolicy.getId();

        // Prepare a DTO with a different id
        var updateDto = new InsurancePolicyDTO(validId + 1, "Test Path/DTO Id Updated", PolicyStatus.ACTIVE, LocalDate.now(), LocalDate.now().plusYears(1), LocalDate.now(), LocalDate.now());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<InsurancePolicyDTO> entity = new HttpEntity<>(updateDto, headers);

        // Send PUT request with mismatched ids
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/policies/" + validId,
                HttpMethod.PUT,
                entity,
                String.class
        );

        // Assert 400 Bad Request and RFC 7807 format
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getHeaders().getContentType()).hasToString("application/problem+json");
        assertThat(response.getBody()).contains("Path id and DTO id must be identical");
    }
}
