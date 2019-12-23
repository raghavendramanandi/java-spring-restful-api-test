package uk.co.huntersix.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.huntersix.spring.rest.exception.PersonNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.List;

@RestController
public class PersonController {
    private PersonDataService personDataService;

    public PersonController(@Autowired PersonDataService personDataService) {
        this.personDataService = personDataService;
    }

    @GetMapping("/person/{lastName}/{firstName}")
    public Person person(@PathVariable(value="lastName") String lastName,
                         @PathVariable(value="firstName") String firstName) throws PersonNotFoundException {
        return personDataService.findPerson(lastName, firstName);
    }

    @PutMapping("/person/{lastName}/{firstName}")
    public Person putPerson(@PathVariable(value="lastName") String lastName,
                         @PathVariable(value="firstName") String firstName) throws PersonNotFoundException {
        return personDataService.addPerson(lastName, firstName);
    }

    @GetMapping("/person/{lastName}")
    public List<Person> getAllPeople(@PathVariable(value="lastName") String lastName) throws PersonNotFoundException {
        return personDataService.findPersonWithLastName(lastName);
    }
}