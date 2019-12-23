package uk.co.huntersix.spring.rest.referencedata;

import org.springframework.stereotype.Service;
import uk.co.huntersix.spring.rest.exception.PersonNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonDataService {
    public static List<Person> PERSON_DATA = null;

    public PersonDataService(){
        PERSON_DATA = new ArrayList<>();
        PERSON_DATA.add(new Person("Mary", "Smith"));
        PERSON_DATA.add(new Person("Brian", "Archer"));
        PERSON_DATA.add(new Person("Collin", "Brown"));

    }

    public Person findPerson(String lastName, String firstName) throws PersonNotFoundException {
        Optional<Person> person = Optional.of(getPersonWith(lastName, firstName));
        return person.orElseThrow(() -> new PersonNotFoundException(firstName, lastName));
    }

    private Person getPersonWith(String lastName, String firstName) {
        return PERSON_DATA.stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                        && p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList()).get(0);
    }

    public List<Person> findPersonWithLastName(String lastName) throws PersonNotFoundException {
        return PERSON_DATA.stream()
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }

    public Person addPerson(String lastName, String firstName) throws PersonNotFoundException {
        Person p = getPersonWith(firstName, lastName);
        if(p == null){
            p = new Person(firstName, lastName);
            PERSON_DATA.add(p);
        }
        return p;
    }
}
