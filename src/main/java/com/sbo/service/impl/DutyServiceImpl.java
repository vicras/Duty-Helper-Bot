package com.sbo.service.impl;

import com.sbo.common.utils.StreamUtil;
import com.sbo.entity.Duty;
import com.sbo.entity.PeopleOnDuty;
import com.sbo.entity.Person;
import com.sbo.exception.EntityNotFoundException;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.repository.DutyRepository;
import com.sbo.service.DutyService;
import com.sbo.service.PeopleOnDutyService;
import com.sbo.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DutyServiceImpl implements DutyService {

    private final CurrentPersonProvider personProvider;
    private final DutyRepository dutyRepository;
    private final PersonService personService;
    private final PeopleOnDutyService peopleOnDutyService;

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
    @Deprecated
    public List<Duty> getDutiesOnDay(LocalDate localDate) {
        return dutyRepository.findAllByDutyFrom(localDate);
    }

    @Override
    public Page<Duty> getDutiesPageOnADay(LocalDate localDate, Pageable pageable) {
        return dutyRepository.findAllByDutyFrom(localDate, pageable);
    }

    @Override
    public Duty getDutyById(Long dutyId) {
        return dutyRepository.findById(dutyId)
                .orElseThrow(() -> new EntityNotFoundException(Duty.class, dutyId));
    }

    public List<Person> getPartnersOfCurrentPersonOn(Duty duty) {
        Person currentPerson = personProvider.getCurrentPerson();
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
                intervals, peopleOnDuty -> peopleOnDutyService.doWorkOnTheSameTime(peopleOnDuty, person)
        ).isEmpty();
    }

}
