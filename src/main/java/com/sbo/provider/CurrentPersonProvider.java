package com.sbo.provider;

import com.sbo.entity.Person;
import com.sbo.exception.EntityNotFoundException;
import com.sbo.exception.UserNameIsNullException;
import com.sbo.service.PersonService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;


/**
 * @author viktar hraskou
 */
@Getter
@Component()
@RequiredArgsConstructor
@Scope(value = SCOPE_PROTOTYPE)
public class CurrentPersonProvider {

    private final PersonService personService;
    private Person currentPerson;

    public void setPersonById(Long telegramId) throws EntityNotFoundException, UserNameIsNullException {
        currentPerson = personService.blockPersonByTelegramId(telegramId);
    }
}
