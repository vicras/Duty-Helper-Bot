package com.sbo.service;

import com.google.common.collect.Range;
import com.sbo.domain.postgres.entity.PeopleOnDuty;
import com.sbo.domain.postgres.entity.Person;
import com.sbo.domain.redis.entity.DutyChangeDataHolder;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author viktar hraskou
 */
public interface ChangeRequestService {

    void updateChangeRequest(PeopleOnDuty peopleOnDuty);

    void updateChangeRequest(Range<LocalDateTime> range);

    void updateChangeRequestWithSameDay(Range<LocalTime> range);

    void useFullRange();

    void sendRequestIfComplete();

    boolean isDataComplete();

    void saveIncompleteDataForPerson(Person person, DutyChangeDataHolder holder);

    DutyChangeDataHolder getIncompleteData(Person person);

    void deleteDataForPerson(Person person);

    boolean isDataComplete(DutyChangeDataHolder holder);

    boolean isPersonAlreadyFilledInHolder(Person person);

}
