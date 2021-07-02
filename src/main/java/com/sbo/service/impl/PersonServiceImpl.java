package com.sbo.service.impl;

import com.sbo.bot.state.State;
import com.sbo.entity.Person;
import com.sbo.entity.enums.PersonRole;
import com.sbo.exception.EntityNotFoundException;
import com.sbo.repository.PersonRepository;
import com.sbo.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static com.sbo.entity.enums.EntityStatus.ACTIVE;
import static com.sbo.entity.enums.EntityStatus.DELETED;
import static com.sbo.entity.enums.PersonRole.ADMIN;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Override
    public Person getPersonByTelegramId(Long telegramId) {
        return personRepository.findPersonByTelegramId(telegramId)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException(Person.class, telegramId);
                });
    }

    @Override
    public Person getPersonById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException(Person.class, id);
                });
    }

    @Override
    public Person updatePersonName(Long telegramID, String name) {
        var person = getPersonByTelegramId(telegramID);
        person.setFirstName(name);

        return personRepository.save(person);
    }

    @Override
    public Person updatePersonLastName(Long telegramID, String lastName) {
        var person = getPersonByTelegramId(telegramID);
        person.setLastName(lastName);

        return personRepository.save(person);
    }

    @Override
    public Person updatePersonPatronymic(Long telegramID, String patronymic) {
        var person = getPersonByTelegramId(telegramID);
        person.setPatronymic(patronymic);

        return personRepository.save(person);
    }

    @Override
    public Person updatePersonTel(Long telegramID, Long tel) {
        var person = getPersonByTelegramId(telegramID);
        person.setTel(tel);

        return personRepository.save(person);
    }

    @Override
    public Person updatePersonBirth(Long telegramID, LocalDate birth) {
        var person = getPersonByTelegramId(telegramID);
        person.setBirthDate(birth);

        return personRepository.save(person);
    }

    @Override
    public Person updatePersonMail(Long telegramID, String mail) {
        var person = getPersonByTelegramId(telegramID);
        person.setMail(mail);

        return personRepository.save(person);
    }

    @Override
    public Person updatePersonHomeAddress(Long telegramID, String address) {
        var person = getPersonByTelegramId(telegramID);
        person.setHomeAddress(address);

        return personRepository.save(person);
    }

    @Override
    public Person addEmptyPersonWithTelegramIdAndRole(Long telegramId, Set<PersonRole> personRoles) {
        var newPerson = Person.builder()
                .telegramId(telegramId)
                .roles(personRoles)
                .build();

        return personRepository.save(newPerson);
    }

    @Override
    public Person blockPersonByTelegramId(Long telegramId) {
        var person = getPersonByTelegramId(telegramId);
        person.setEntityStatus(DELETED);

        return personRepository.save(person);
    }

    @Override
    public Person unblockPersonByTelegramId(Long telegramId) {
        var person = getPersonByTelegramId(telegramId);
        person.setEntityStatus(ACTIVE);

        return personRepository.save(person);
    }

    @Override
    public List<Person> getActivePersons() {
        return personRepository.getAllByEntityStatusIn(List.of(ACTIVE));
    }

    @Override
    public List<Person> getActiveAdmins() {
        return personRepository.getAllByEntityStatusInAndRoles(List.of(ACTIVE), (ADMIN));
    }

    @Override
    public void updateState(Person person, State state) {
        person.setState(state.getClass().toString());
    }


}
