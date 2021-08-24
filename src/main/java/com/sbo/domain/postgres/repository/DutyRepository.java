package com.sbo.domain.postgres.repository;

import com.sbo.domain.postgres.entity.Duty;
import com.sbo.domain.postgres.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


public interface DutyRepository extends JpaRepository<Duty, Long> {

    //region all duty in day
    @Query(value = "select d from Duty d" +
            " where d.dutyFrom > :#{#day.atStartOfDay()} " +
            "  and d.dutyFrom < :#{#day.plusDays(1).atStartOfDay()} ")
    Page<Duty> findAllDutyInDay(LocalDate day, Pageable pageable);

    @Query(value = "select d from Duty d" +
            " where d.dutyFrom > :#{#day.atStartOfDay()} " +
            "  and d.dutyFrom < :#{#day.plusDays(1).atStartOfDay()} ")
    List<Duty> findAllDutyInDay(LocalDate day);
    //endregion

    // flexible method
    @Query(value = "select d " +
            "from Duty d " +
            "where d.dutyFrom > :#{#day.atStartOfDay()} " +
            "  and d.dutyFrom < :#{#day.plusDays(1).atStartOfDay()} " +
            "  and d not IN ( select pod2.duty from PeopleOnDuty pod2 where pod2.person in :person ) ")
    Page<Duty> findAllDutyInDayWithoutPersons(LocalDate day,
                                              Collection<Person> person,
                                              Pageable pageable);

    @Query(value = "select d " +
            "from Duty d " +
            "left join d.peopleOnDuties pod " +
            "where d.dutyFrom > :#{#day.atStartOfDay()} " +
            "  and d.dutyFrom < :#{#day.plusDays(1).atStartOfDay()} " +
            "  and pod.person = :person")
    Page<Duty> findAllDutyInDayWithPerson(LocalDate day,
                                          Person person,
                                          Pageable pageable);

    // flexible method
    @Query(value = "select d " +
            "from Duty d " +
            "left join d.peopleOnDuties pod " +
            "where d.dutyFrom > :#{#day.atStartOfDay()} " +
            "  and d.dutyFrom < :#{#day.plusDays(1).atStartOfDay()} " +
            "  and pod.person in :withPersons " +
            "  and d not in ( select pod2.duty from PeopleOnDuty pod2 where pod.person in :withoutPersons)")
    Page<Duty> findAllDutyInDayWithPersonsAndWithoutPersons(LocalDate day,
                                                            Collection<Person> withPersons,
                                                            Collection<Person> withoutPersons,
                                                            Pageable pageable);

    //region all duty of person
    @Query(value = "select d " +
            " from Duty d" +
            " join d.peopleOnDuties pod" +
            " where pod.person = :person")
    List<Duty> findAllDutyOfPerson(Person person);

    @Query(value = "select d " +
            " from Duty d" +
            " join d.peopleOnDuties pod" +
            " where pod.person = :person")
    Page<Duty> findAllDutyOfPerson(Person person, Pageable pageable);
    //endregion

    //region all duty without person
    @Query(value = "select d " +
            " from Duty d " +
            " left join d.peopleOnDuties pod " +
            " where d not IN ( select pod2.duty from PeopleOnDuty pod2 where pod2.person in :person )")
    List<Duty> findAllDutyWithoutPerson(Person person);

    @Query(value = "select d " +
            " from Duty d " +
            " left join d.peopleOnDuties pod " +
            " where d not IN ( select pod2.duty from PeopleOnDuty pod2 where pod2.person in :person )")
    Page<Duty> findAllDutyWithoutPerson(Person person, Pageable pageable);
    //endregion
}
