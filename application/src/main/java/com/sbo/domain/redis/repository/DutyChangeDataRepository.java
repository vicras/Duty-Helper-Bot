package com.sbo.domain.redis.repository;

import com.sbo.domain.postgres.entity.Person;
import com.sbo.domain.redis.entity.DutyChangeDataHolder;

import java.util.Optional;

/**
 * @author viktar hraskou
 */
public interface DutyChangeDataRepository {
    void saveDutyChangeData(Person person, DutyChangeDataHolder dataHolder);

    void editDutyChangeData(Person person, DutyChangeDataHolder dataHolder);

    Optional<DutyChangeDataHolder> findDutyChangeData(Person person);

    void deleteDutyChangeData(Person person);
}
