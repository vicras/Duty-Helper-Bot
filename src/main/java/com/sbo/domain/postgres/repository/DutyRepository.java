package com.sbo.domain.postgres.repository;

import com.sbo.domain.postgres.entity.Duty;
import com.sbo.domain.postgres.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;


public interface DutyRepository extends JpaRepository<Duty, Long> {
    @Query(value = "select * from duties d where DATE(duty_from) = :dutyFrom", nativeQuery = true)
    Page<Duty> findAllByDutyFrom(LocalDate dutyFrom, Pageable pageable);

    @Query(value = "select * from duties d left join person p where DATE(duty_from) = :dutyFrom and p.id <> :#{#person.id}", nativeQuery = true)
    Page<Duty> findAllByDutyFromWithoutPerson(LocalDate dutyFrom, Person person, Pageable pageable);

    @Query(value = "select * from duties d left join person p where DATE(duty_from) = :dutyFrom and p.id = :#{#person.id}", nativeQuery = true)
    Page<Duty> findAllByDutyFromWithOnlyPerson(LocalDate dutyFrom, Person person, Pageable pageable);

    @Query(value = "select * from duties d where DATE(duty_from) = :dutyFrom", nativeQuery = true)
    List<Duty> findAllByDutyFrom(LocalDate dutyFrom);

    @Query(value = "select d from Duty d join Person p where p.id = :#{#person.id}")
    List<Duty> findAllDutyOfPerson(Person person);

    @Query(value = "select d from Duty d join Person p where p.id = :#{#person.id}")
    Page<Duty> findAllDutyOfPerson(Person person, Pageable pageable);

    @Query(value = "select d from Duty d join Person p where p.id <> :#{#person.id}")
    List<Duty> findAllDutyWithoutPerson(Person person);

    @Query(value = "select d from Duty d join Person p where p.id <> :#{#person.id}")
    Page<Duty> findAllDutyWithoutPerson(Person person, Pageable pageable);
}
