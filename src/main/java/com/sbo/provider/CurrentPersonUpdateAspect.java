package com.sbo.provider;

import com.sbo.entity.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * @author viktar hraskou
 */
@Slf4j
@Aspect
@Component
@Transactional
@RequiredArgsConstructor
public class CurrentPersonUpdateAspect {

    private final CurrentPersonProvider provider;

    @Pointcut("execution(* com.sbo.service.impl.PersonServiceImpl.update*(..))")
    public void allPersonUpdateMethods() {
    }

    @AfterReturning(pointcut = "allPersonUpdateMethods()", returning = "person")
    public void afterReturningAdvice(Person person) {
        if (person.getTelegramId().equals(provider.getCurrentPersonId())) {
            Hibernate.initialize(person.getRoles());
            provider.setPerson(person);
            log.info("current person state in provider changed");
        }
    }

}
