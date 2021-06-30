package com.sbo.service;

import com.sbo.entity.Person;
import com.sbo.entity.enums.PersonRole;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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

    Person addEmptyPersonWithTelegramIdAndRole(Long telegramId, Set<PersonRole> personRoles);

    Person blockPersonByTelegramId(Long telegramId);

    Person unblockPersonByTelegramId(Long telegramId);

    List<Person> getActivePersons();

    List<Person> getActiveAdmins();

}
