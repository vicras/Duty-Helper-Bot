package com.sbo.repository;

import com.sbo.entity.Duty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface DutyRepository extends JpaRepository<Duty, Long> {
    @Query(value = "select * from duties d where DATE(duty_from) = :dutyFrom", nativeQuery = true)
    Page<Duty> findAllByDutyFrom(@Param("dutyFrom") LocalDate dutyFrom, Pageable pageable);

    @Query(value = "select * from duties d where DATE(duty_from) = :dutyFrom", nativeQuery = true)
    List<Duty> findAllByDutyFrom(LocalDate dutyFrom);
}
