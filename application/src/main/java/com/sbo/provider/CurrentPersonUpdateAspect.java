package com.sbo.provider;

import com.sbo.domain.postgres.entity.Person;
import com.sbo.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author viktar hraskou
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CurrentPersonUpdateAspect {

    private final CurrentPersonProvider provider;
    private final PersonService personService;

    @Pointcut("execution(* com.sbo.service.impl.PersonServiceImpl.update*(..))")
    public void allPersonUpdateMethods() {
    }

    @AfterReturning(pointcut = "allPersonUpdateMethods()", returning = "person")
    public void afterReturningAdvice(Person person) {
        if (person.getTelegramId().equals(provider.getCurrentPersonId())) {
            person = personService.initializePersonRoles(person);
            provider.setPerson(person);
            log.info("current person state in provider changed");
        }
    }

}
