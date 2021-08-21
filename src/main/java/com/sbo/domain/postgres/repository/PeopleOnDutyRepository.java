package com.sbo.domain.postgres.repository;

import com.sbo.domain.postgres.entity.PeopleOnDuty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeopleOnDutyRepository extends JpaRepository<PeopleOnDuty, Long> {
}
