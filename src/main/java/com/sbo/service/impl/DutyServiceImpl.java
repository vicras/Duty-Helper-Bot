package com.sbo.service.impl;

import com.sbo.common.utils.StreamUtil;
import com.sbo.entity.Duty;
import com.sbo.entity.PeopleOnDuty;
import com.sbo.entity.Person;
import com.sbo.repo.DutyRepository;
import com.sbo.service.DutyService;
import com.sbo.service.PeopleOnDutyService;
import com.sbo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DutyServiceImpl implements DutyService {
    @Autowired
    private DutyRepository dutyRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private PeopleOnDutyService peopleOnDutyService;

    @Override
    public List<Duty> getPersonDuties(Person person) {
        return StreamUtil.filter(dutyRepository.findAll(),
                duty -> duty.getPeopleOnDuties().stream()
                        .anyMatch(
                                peopleOnDuty -> peopleOnDuty.getPerson().equals(person)
                        )
        );
    }

    @Override
    public List<Duty> getDutiesOnDay(LocalDate localDate) {
        return dutyRepository.findAll().stream()
                .filter(duty -> duty.getDutyFrom().getYear() == localDate.getYear()
                        && duty.getDutyFrom().getDayOfYear() == localDate.getDayOfYear())
                .collect(Collectors.toList());
    }

    public List<Person> getPartnersOfCurrentPersonOn(Duty duty) {
        Person currentPerson = personService.getCurrentPerson();
        List<PeopleOnDuty> currentUserOnDuty = duty.getPeopleOnDuties().stream()
                .filter(peopleOnDuty -> peopleOnDuty.getPerson().equals(currentPerson))
                .collect(Collectors.toList());
        duty.getPeopleOnDuties().removeIf(peopleOnDuty -> peopleOnDuty.getPerson().equals(currentPerson));
        List<PeopleOnDuty> partnersOnDuty = getWhoWorksWithUser(duty.getPeopleOnDuties(), currentUserOnDuty);
        return peopleOnDutyService.getPersonsInPeopleOnDuties(partnersOnDuty);
    }

    private List<PeopleOnDuty> getWhoWorksWithUser(Collection<PeopleOnDuty> peopleOnDuties,
                                                   List<PeopleOnDuty> currentUserOnDuty) {
        return peopleOnDuties.stream()
                .filter(peopleOnDuty -> workOnTimeWithPersonIntervals(currentUserOnDuty, peopleOnDuty))
                .collect(Collectors.toList());
    }

    private boolean workOnTimeWithPersonIntervals(List<PeopleOnDuty> intervals, PeopleOnDuty person) {
        return !StreamUtil.filter(
                intervals,peopleOnDuty -> peopleOnDutyService.doWorkOnTheSameTime(peopleOnDuty, person)
        ).isEmpty();
    }

}
