package com.sbo.service;

import com.sbo.domain.postgres.entity.PeopleOnDuty;
import com.sbo.domain.postgres.entity.Person;

import java.util.List;

public interface PersonOnDutyService {
    PeopleOnDuty getPeopleOnDutyById(Long peopleOnDutyId);

    List<PeopleOnDuty> getPeopleOnDutiesOfPerson(Person person);

    List<PeopleOnDuty> getFuturePeopleOnDutiesOfPerson(Person person);

    List<PeopleOnDuty> getWhoWorksWithUser(List<PeopleOnDuty> peopleOnDuties,
                                           List<PeopleOnDuty> currentUserOnDuty);

    boolean doWorkOnTheSameTime(PeopleOnDuty first, PeopleOnDuty second);

    List<Person> getPersonsInPeopleOnDuties(List<PeopleOnDuty> peopleOnDutyList);
}
