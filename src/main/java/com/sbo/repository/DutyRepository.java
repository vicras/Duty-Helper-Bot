package com.sbo.repository;

import com.sbo.entity.Duty;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DutyRepository extends JpaRepository<Duty, Long> {
}
