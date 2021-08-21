package com.sbo.service;

import com.sbo.domain.postgres.entity.Duty;
import com.sbo.domain.postgres.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface DutyService {
    List<Duty> getPersonDuties(Person person);

    List<Duty> getDutiesOnDay(LocalDate localDate);

    Page<Duty> getDutiesPageOnADay(LocalDate localDate, Pageable pageable);

    Duty getDutyById(Long dutyId);
}
