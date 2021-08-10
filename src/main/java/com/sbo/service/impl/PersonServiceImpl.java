package com.sbo.service.impl;

import com.sbo.bot.state.State;
import com.sbo.entity.Person;
import com.sbo.entity.enums.Language;
import com.sbo.entity.enums.PersonRole;
import com.sbo.exception.EntityNotFoundException;
import com.sbo.repository.PersonRepository;
import com.sbo.service.PersonService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sbo.entity.enums.EntityStatus.ACTIVE;
import static com.sbo.entity.enums.EntityStatus.DELETED;
import static com.sbo.entity.enums.PersonRole.ADMIN;
import static java.util.Objects.nonNull;

@Slf4j
@Service
@Transactional
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

        log.info("Set first name={} for user with id={} ", name, telegramID);
        return personRepository.save(person);
    }

    @Override
    public Person updatePersonLastName(Long telegramID, String lastName) {
        var person = getPersonByTelegramId(telegramID);
        person.setLastName(lastName);

        log.info("Set last name={} for user with id={} ", lastName, telegramID);
        return personRepository.save(person);
    }

    @Override
    public Person updatePersonPatronymic(Long telegramID, String patronymic) {
        var person = getPersonByTelegramId(telegramID);
        person.setPatronymic(patronymic);

        log.info("Set patronymic={} for user with id={} ", patronymic, telegramID);
        return personRepository.save(person);
    }

    @Override
    public Person updatePersonTel(Long telegramID, Long tel) {
        var person = getPersonByTelegramId(telegramID);
        person.setTel(tel);

        log.info("Set telephone={} for user with id={} ", tel, telegramID);
        return personRepository.save(person);
    }

    @Override
    public Person updatePersonBirth(Long telegramID, LocalDate birth) {
        var person = getPersonByTelegramId(telegramID);
        person.setBirthDate(birth);

        log.info("Set birth={} for user with id={} ", birth, telegramID);
        return personRepository.save(person);
    }

    @Override
    public Person updatePersonMail(Long telegramID, String mail) {
        var person = getPersonByTelegramId(telegramID);
        person.setMail(mail);

        log.info("Set mail={} for user with id={} ", mail, telegramID);
        return personRepository.save(person);
    }

    @Override
    public Person updatePersonHomeAddress(Long telegramID, String address) {
        var person = getPersonByTelegramId(telegramID);
        person.setHomeAddress(address);

        log.info("Set address={} for user with id={} ", address, telegramID);
        return personRepository.save(person);
    }

    @Override
    public Person updatePersonState(Long telegramId, State state) {
        var person = getPersonByTelegramId(telegramId);
        person.setState(state.getClass().toString());

        log.info("Set state={} for user with id={} ", state, telegramId);
        return personRepository.save(person);
    }

    @Override
    public Person updatePersonLanguage(Long telegramId, Language language) {
        var person = getPersonByTelegramId(telegramId);
        person.setLanguage(language);

        log.info("Set language={} for user with id={} ", language, telegramId);
        return personRepository.save(person);
    }

    @Override
    public Person updatePersonRoles(Long telegramId, Collection<PersonRole> personRoles) {
        var person = getPersonByTelegramId(telegramId);
        person.setRoles(new HashSet<>(personRoles));

        log.info("Set language={} for user with id={} ", personRoles, telegramId);
        return personRepository.save(person);
    }

    @Override
    public Person addEmptyPersonWithTelegramIdAndRole(@NonNull Long telegramId, Set<PersonRole> personRoles) {
        var newPerson = Person.builder()
                .telegramId(telegramId)
                .roles(personRoles)
                .build();

        log.info("Set roles={} for user with id={} ", personRoles, telegramId);
        return personRepository.save(newPerson);
    }

    @Override
    public Person blockPersonByTelegramId(Long telegramId) {
        var person = getPersonByTelegramId(telegramId);
        person.setEntityStatus(DELETED);

        log.info("Ban user with id={} ", telegramId);
        return personRepository.save(person);
    }

    @Override
    public Person unblockPersonByTelegramId(Long telegramId) {
        var person = getPersonByTelegramId(telegramId);
        person.setEntityStatus(ACTIVE);

        log.info("Unban user with id={} ", telegramId);
        return personRepository.save(person);
    }

    @Override
    public Page<Person> getActivePersons(Pageable pageable) {
        return personRepository.getAllByEntityStatusIn(List.of(ACTIVE), pageable);
    }

    @Override
    public Page<Person> getBlockedPersons(Pageable pageable) {
        return personRepository.getAllByEntityStatusIn(List.of(DELETED), pageable);
    }

    @Override
    public List<Person> getActiveAdmins() {
        return personRepository.getAllByEntityStatusInAndRoles(List.of(ACTIVE), (ADMIN));
    }

    @Override
    public Person initializePersonRoles(Person person) {
        Hibernate.initialize(person.getRoles());
        return person;
    }

    @Override
    public boolean isPersonInfoFiled(Long telegramId) {
        Person person = getPersonByTelegramId(telegramId);
        return isPersonInfoFiled(person);
    }

    @Override
    public boolean isPersonInfoFiled(Person person) {
        return nonNull(person.getFirstName())
                && nonNull(person.getLastName())
                && nonNull(person.getPatronymic())
                && nonNull(person.getBirthDate())
                && nonNull(person.getTel())
                && nonNull(person.getMail())
                && nonNull(person.getHomeAddress());
    }

    @Override
    public boolean isPersonExist(Long telegramId) {
        return personRepository.existsByTelegramId(telegramId);
    }
}
