package com.sbo.service;

import com.sbo.entity.PeopleOnDuty;
import com.sbo.entity.Person;

import java.util.List;

public interface PeopleOnDutyService {
    List<PeopleOnDuty> getPeopleOnDutiesOfPerson(Person person);

    List<PeopleOnDuty> getFuturePeopleOnDutiesOfPerson(Person person);

    List<PeopleOnDuty> getWhoWorksWithUser(List<PeopleOnDuty> peopleOnDuties,
                                           List<PeopleOnDuty> currentUserOnDuty);

    boolean doWorkOnTheSameTime(PeopleOnDuty first, PeopleOnDuty second);

    List<Person> getPersonsInPeopleOnDuties(List<PeopleOnDuty> peopleOnDutyList);
}
