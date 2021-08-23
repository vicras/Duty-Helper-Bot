package com.sbo.service.impl;

import com.sbo.common.utils.StreamUtil;
import com.sbo.domain.postgres.entity.Duty;
import com.sbo.domain.postgres.entity.PeopleOnDuty;
import com.sbo.domain.postgres.entity.Person;
import com.sbo.domain.postgres.repository.DutyRepository;
import com.sbo.exception.EntityNotFoundException;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.DutyService;
import com.sbo.service.PersonOnDutyService;
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
    private final PersonOnDutyService personOnDutyService;

    @Override
    public List<Duty> getPersonDuties(Person person) {
        return dutyRepository.findAllDutyOfPerson(person);
    }

    @Override
    public Page<Duty> getPersonDuties(Person person, Pageable pageable) {
        return dutyRepository.findAllDutyOfPerson(person, pageable);
    }

    public List<Duty> getDutiesWithoutPerson(Person person) {
        return dutyRepository.findAllDutyWithoutPerson(person);
    }

    @Override
    public Page<Duty> getDutiesWithoutPerson(Person person, Pageable pageable) {
        return dutyRepository.findAllDutyWithoutPerson(person, pageable);
    }

    @Override
    @Deprecated
    public List<Duty> getDutiesOnDay(LocalDate localDate) {
        return dutyRepository.findAllByDutyFrom(localDate);
    }

    @Override
    public Page<Duty> getPageWithAllDutiesOnADay(LocalDate localDate, Pageable pageable) {
        return dutyRepository.findAllByDutyFrom(localDate, pageable);
    }

    @Override
    public Page<Duty> getPageWithOnlyMyDutiesOnADay(LocalDate localDate, Pageable pageable) {
        return dutyRepository.findAllByDutyFromWithOnlyPerson(localDate, personProvider.getCurrentPerson(), pageable);
    }

    @Override
    public Page<Duty> getPageWithoutMyDutiesOnADay(LocalDate localDate, Pageable pageable) {
        return dutyRepository.findAllByDutyFromWithoutPerson(localDate, personProvider.getCurrentPerson(), pageable);
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
        return personOnDutyService.getPersonsInPeopleOnDuties(partnersOnDuty);
    }

    private List<PeopleOnDuty> getWhoWorksWithUser(Collection<PeopleOnDuty> peopleOnDuties,
                                                   List<PeopleOnDuty> currentUserOnDuty) {
        return peopleOnDuties.stream()
                .filter(peopleOnDuty -> workOnTimeWithPersonIntervals(currentUserOnDuty, peopleOnDuty))
                .collect(Collectors.toList());
    }

    private boolean workOnTimeWithPersonIntervals(List<PeopleOnDuty> intervals, PeopleOnDuty person) {
        return !StreamUtil.filter(
                intervals, peopleOnDuty -> personOnDutyService.doWorkOnTheSameTime(peopleOnDuty, person)
        ).isEmpty();
    }

}
