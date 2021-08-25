package com.sbo.domain.redis.repository.impl;

import com.sbo.domain.postgres.entity.Person;
import com.sbo.domain.redis.entity.DutyChangeDataHolder;
import com.sbo.domain.redis.repository.DutyChangeDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * @author viktar hraskou
 */
@Repository
public class DutyChangeDataRepositoryImpl implements DutyChangeDataRepository {

    private static final String KEY = "DUTY_CHANGE_DATA";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, Long, DutyChangeDataHolder> hashOperations;

    @Autowired
    public DutyChangeDataRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void saveDutyChangeData(Person person, DutyChangeDataHolder dataHolder) {
        hashOperations.put(KEY, person.getId(), dataHolder);
    }

    @Override
    public void editDutyChangeData(Person person, DutyChangeDataHolder dataHolder) {
        saveDutyChangeData(person, dataHolder);
    }

    @Override
    public Optional<DutyChangeDataHolder> findDutyChangeData(Person person) {
        return Optional.ofNullable(hashOperations.get(KEY, person.getId()));
    }

    @Override
    public void deleteDutyChangeData(Person person) {
        hashOperations.delete(KEY, person.getId());
    }
}
