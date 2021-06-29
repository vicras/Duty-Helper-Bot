package com.sbo.service.impl;

import com.sbo.common.time.LocalDateTimeInterval;
import com.sbo.common.utils.StreamUtil;
import com.sbo.entity.PeopleOnDuty;
import com.sbo.entity.Person;
import com.sbo.repository.PeopleOnDutyRepository;
import com.sbo.service.PeopleOnDutyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PeopleOnDutyServiceImpl implements PeopleOnDutyService {

    private final PeopleOnDutyRepository repository;

    @Override
    public List<PeopleOnDuty> getPeopleOnDutiesOfPerson(Person person) {
        return StreamUtil.filter(repository.findAll(), peopleOnDuty -> peopleOnDuty.getPerson().equals(person));
    }

    @Override
    public List<PeopleOnDuty> getFuturePeopleOnDutiesOfPerson(Person person) {
        LocalDateTime now = LocalDateTime.now();
        var personsOnDuties = getPeopleOnDutiesOfPerson(person);
        return StreamUtil.filter(personsOnDuties, peopleOnDuty -> peopleOnDuty.getOnDutyFrom().isAfter(now));
    }

    @Override
    public List<PeopleOnDuty> getWhoWorksWithUser(List<PeopleOnDuty> peopleOnDuties, List<PeopleOnDuty> currentUserOnDuty) {
        return StreamUtil.filter(peopleOnDuties, peopleOnDuty -> workOnTimeWithPersonIntervals(currentUserOnDuty, peopleOnDuty));
    }

    private boolean workOnTimeWithPersonIntervals(List<PeopleOnDuty> intervals, PeopleOnDuty person) {
        return !StreamUtil.filter(intervals, peopleOnDuty -> doWorkOnTheSameTime(peopleOnDuty, person)).isEmpty();
    }

    public boolean doWorkOnTheSameTime(PeopleOnDuty first, PeopleOnDuty second) {
        LocalDateTimeInterval firstInterval = first.getWorkInterval();
        LocalDateTimeInterval secondInterval = second.getWorkInterval();
        return firstInterval.intersects(secondInterval);
    }

    @Override
    public List<Person> getPersonsInPeopleOnDuties(List<PeopleOnDuty> peopleOnDutyList) {
        List<Person> persons = new ArrayList<>();
        Set<Person> userIds = new HashSet<>();
        for (PeopleOnDuty peopleOnDuty : peopleOnDutyList) {
            Person person = peopleOnDuty.getPerson();
            if (!userIds.contains(person)) {
                persons.add(person);
                userIds.add(person);
            }
        }
        return persons;
    }
}
