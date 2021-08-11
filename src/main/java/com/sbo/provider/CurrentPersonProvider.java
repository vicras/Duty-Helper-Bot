package com.sbo.provider;

import com.sbo.entity.Person;
import com.sbo.exception.EntityNotFoundException;
import com.sbo.exception.UserNameIsNullException;
import com.sbo.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * @author viktar hraskou
 */
@Component
@RequiredArgsConstructor
public class CurrentPersonProvider {

    private final PersonService personService;

    private final ThreadLocal<Person> currentPerson = ThreadLocal.withInitial(Person::new);

    public Person getCurrentPerson() {
        return currentPerson.get();
    }

    public Long getCurrentPersonId() {
        return currentPerson.get().getTelegramId();
    }

    public void setPerson(Person person) {
        person = personService.initializePersonRoles(person);
        currentPerson.set(person);
    }
}
