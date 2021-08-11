package com.sbo.service;

import com.sbo.bot.state.State;
import com.sbo.entity.Person;
import com.sbo.entity.enums.Language;
import com.sbo.entity.enums.PersonRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

//@Transactional
public interface PersonService {
    Person getPersonByTelegramId(Long telegramId);

    Person getPersonById(Long id);

    Person updatePersonName(Long telegramID, String name);

    Person updatePersonLastName(Long telegramID, String lastName);

    Person updatePersonPatronymic(Long telegramID, String patronymic);

    Person updatePersonTel(Long telegramID, Long tel);

    Person updatePersonBirth(Long telegramID, LocalDate birth);

    Person updatePersonMail(Long telegramID, String mail);

    Person updatePersonHomeAddress(Long telegramID, String address);

    Person updatePersonState(Long telegramId, State state);

    Person updatePersonLanguage(Long telegramId, Language language);

    Person updatePersonRoles(Long telegramId, Collection<PersonRole> personRoles);

    Person addEmptyPersonWithTelegramIdAndRole(Long telegramId, Set<PersonRole> personRoles);

    Person blockPersonByTelegramId(Long telegramId);

    Person unblockPersonByTelegramId(Long telegramId);

    Page<Person> getActivePersons(Pageable pageable);

    Page<Person> getBlockedPersons(Pageable pageable);

    List<Person> getActiveAdmins();

    //    @Transactional(readOnly = true)
    Person initializePersonRoles(Person person);

    boolean isPersonInfoFiled(Long telegramId);

    boolean isPersonInfoFiled(Person person);

    boolean isPersonExist(Long telegramId);
}
