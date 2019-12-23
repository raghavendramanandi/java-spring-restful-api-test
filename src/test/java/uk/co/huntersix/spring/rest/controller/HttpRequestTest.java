package uk.co.huntersix.spring.rest.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturnPersonDetails() {
        assertThat(
            this.restTemplate.getForObject(
                "http://localhost:" + port + "/person/smith/mary",
                String.class
            )
        ).contains("Mary");
    }

    @Test
    public void ShouldReturnAllPeopleEndingWithGivenLastName() {
        assertThat(
                this.restTemplate.getForObject
                        ("http://localhost:" + port + "/person/Brown", String.class))
                .contains("Brown")
                .contains("Collin");
    }

    @Test
    public void ShouldReturnThePersonReferenceAfterAddingAnNewPerson() {
        ResponseEntity<String> resp =  this.restTemplate.exchange("http://localhost:" + port + "/person/Brown/Blue", HttpMethod.PUT,
                null, String.class);
        assertThat(resp.toString()).contains("Brown").contains("Blue");
    }

    @Test
    public void ShouldReturnThePersonReferenceAfterAddingAnExistingPerson() {
        ResponseEntity<String> resp =  this.restTemplate.exchange("http://localhost:" + port + "/person/Mary/Smith", HttpMethod.PUT,
                null, String.class);
        assertThat(resp.toString()).contains("Mary").contains("Smith");
    }
}