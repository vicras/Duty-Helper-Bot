package com.sbo.repository;

import com.sbo.entity.Person;
import com.sbo.entity.enums.EntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.Predicate;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findPersonByTelegramId(@NotEmpty Long telegramId);

    List<Person> getAllByEntityStatusIn(Collection<EntityStatus> entityStatus);
}
