package com.sbo.service.impl;

import com.sbo.common.time.LocalDateTimeInterval;
import com.sbo.common.utils.StreamUtil;
import com.sbo.domain.postgres.entity.PeopleOnDuty;
import com.sbo.domain.postgres.entity.Person;
import com.sbo.domain.postgres.repository.PeopleOnDutyRepository;
import com.sbo.exception.EntityNotFoundException;
import com.sbo.service.PersonOnDutyService;
import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PersonOnDutyServiceImpl implements PersonOnDutyService {

    private final PeopleOnDutyRepository repository;

    @Override
    public PeopleOnDuty getPeopleOnDutyById(Long peopleOnDutyId) {
        return repository.findById(peopleOnDutyId)
                .orElseThrow(() -> new EntityNotFoundException(PeopleOnDuty.class, peopleOnDutyId));
    }

    @Override
    public List<PeopleOnDuty> getPeopleOnDutiesOfPerson(Person person) {
        return StreamUtil.filter(repository.findAll(), peopleOnDuty -> peopleOnDuty.getPerson().equals(person));
    }

    @Override
    public List<PeopleOnDuty> getFuturePeopleOnDutiesOfPerson(Person person) {
        LocalDateTime now = LocalDateTime.now();
        var personsOnDuties = getPeopleOnDutiesOfPerson(person);
        return StreamUtil.filter(personsOnDuties, peopleOnDuty -> peopleOnDuty.getFromTime().isAfter(now));
    }

    @Override
    public List<PeopleOnDuty> getWhoWorksWithUser(List<PeopleOnDuty> peopleOnDuties, List<PeopleOnDuty> currentUserOnDuty) {
        return StreamUtil.filter(peopleOnDuties, peopleOnDuty -> workOnTimeWithPersonIntervals(currentUserOnDuty, peopleOnDuty));
    }

    private boolean workOnTimeWithPersonIntervals(List<PeopleOnDuty> intervals, PeopleOnDuty person) {
        return !StreamUtil.filter(intervals, peopleOnDuty -> doWorkOnTheSameTime(peopleOnDuty, person)).isEmpty();
    }

    public boolean doWorkOnTheSameTime(PeopleOnDuty first, PeopleOnDuty second) {
        throw new NotYetImplementedException();
//        LocalDateTimeInterval firstInterval = first.getWorkInterval();
//        LocalDateTimeInterval secondInterval = second.getWorkInterval();
//        return firstInterval.intersects(secondInterval);
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
