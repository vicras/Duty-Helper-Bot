package com.sbo.service;

import com.sbo.entity.Duty;
import com.sbo.entity.Person;

import java.util.List;

public interface DutyService {
    List<Duty> getPersonDuties(Person person);
}
