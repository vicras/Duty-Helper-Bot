package com.sbo.service;

import com.sbo.domain.postgres.entity.Duty;
import com.sbo.domain.postgres.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface DutyService {
    List<Duty> getPersonDuties(Person person);

    Page<Duty> getPersonDuties(Person person, Pageable pageable);

    List<Duty> getDutiesWithoutPerson(Person person);

    Page<Duty> getDutiesWithoutPerson(Person person, Pageable pageable);

    List<Duty> getDutiesOnDay(LocalDate localDate);

    Page<Duty> getPageWithAllDutiesOnADay(LocalDate localDate, Pageable pageable);

    Page<Duty> getPageWithOnlyMyDutiesOnADay(LocalDate localDate, Pageable pageable);

    Page<Duty> getPageWithoutMyDutiesOnADay(LocalDate localDate, Pageable pageable);

    Duty getDutyById(Long dutyId);
}
