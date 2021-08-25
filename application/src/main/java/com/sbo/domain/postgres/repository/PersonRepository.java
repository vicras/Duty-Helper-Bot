package com.sbo.domain.postgres.repository;

import com.sbo.domain.postgres.entity.Person;
import com.sbo.domain.postgres.entity.enums.EntityStatus;
import com.sbo.domain.postgres.entity.enums.PersonRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findPersonByTelegramId(@NotEmpty Long telegramId);

    Page<Person> getAllByEntityStatusIn(Collection<EntityStatus> entityStatus, Pageable pageable);

    List<Person> getAllByEntityStatusInAndRoles(Collection<EntityStatus> entityStatus, PersonRole role);

    Boolean existsByTelegramId(Long telegramId);
}