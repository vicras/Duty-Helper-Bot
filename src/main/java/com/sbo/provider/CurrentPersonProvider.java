package com.sbo.provider;

import com.sbo.entity.Person;
import com.sbo.exception.EntityNotFoundException;
import com.sbo.exception.UserNameIsNullException;
import com.sbo.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;


/**
 * @author viktar hraskou
 */
@Component
@Transactional
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

    public void setPersonById(Long telegramId) throws EntityNotFoundException, UserNameIsNullException {
        Person person = personService.getPersonByTelegramId(telegramId);
        personService.initializePersonRoles(person);
        currentPerson.set(person);
    }

    public void setPerson(Person person) {
        personService.initializePersonRoles(person);
        currentPerson.set(person);
    }
}
