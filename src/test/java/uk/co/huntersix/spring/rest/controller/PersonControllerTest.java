package uk.co.huntersix.spring.rest.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.co.huntersix.spring.rest.exception.PersonNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonDataService personDataService;

    @Test
    public void shouldReturnPersonFromService() throws Exception {
        when(personDataService.findPerson(any(), any())).thenReturn(new Person("Mary", "Smith"));
        this.mockMvc.perform(get("/person/smith/mary"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("firstName").value("Mary"))
            .andExpect(jsonPath("lastName").value("Smith"));
    }

    @Test
    public void shouldReturnNotFoundIfPersonDoesNotExist() throws Exception {
        PersonNotFoundException e = new PersonNotFoundException("smith", "mary");
        when(personDataService.findPerson(any(), any())).thenThrow(e);
        this.mockMvc.perform(get("/person/smith/mary"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("id").doesNotExist())
                .andExpect(jsonPath("firstName").doesNotExist())
                .andExpect(jsonPath("lastName").doesNotExist());
    }

    @Test
    public void shouldReturnAllPeopleWithLastNameWhenMoreThanOneExists() throws Exception {
        List<Person> personData = Arrays.asList(
                new Person("Mary", "Smith"),
                new Person("Brian", "Smith"),
                new Person("Collin", "Smith")
        );
        when(personDataService.findPersonWithLastName(any())).thenReturn(personData);
        this.mockMvc.perform(get("/person/smith"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].firstName").value("Mary"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[1].firstName").value("Brian"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"))
                .andExpect(jsonPath("$[2].firstName").value("Collin"))
                .andExpect(jsonPath("$[2].lastName").value("Smith"));
    }

    @Test
    public void shouldReturnAllPeopleWithLastNameWhenOneExists() throws Exception {
        List<Person> personData = Arrays.asList(
                new Person("Mary", "Smith")
        );

        when(personDataService.findPersonWithLastName(any())).thenReturn(personData);
        this.mockMvc.perform(get("/person/smith"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("Mary"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"));
    }

    @Test
    public void shouldReturnEmptyWithLastNameWhenDoesExists() throws Exception {
        List<Person> personData = Arrays.asList();
        when(personDataService.findPersonWithLastName(any())).thenReturn(personData);
        this.mockMvc.perform(get("/person/smith"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldReturnPersonThatWasAdded() throws Exception {
        when(personDataService.addPerson(any(), any())).thenReturn(new Person("Steve", "Waugh"));
        this.mockMvc.perform(put("/person/Steve/Waugh"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("firstName").value("Steve"))
                .andExpect(jsonPath("lastName").value("Waugh"));
    }
}