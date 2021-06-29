package com.sbo.provider;

import com.sbo.entity.Person;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;


/**
 * @author viktar hraskou
 */
@Data
@Component()
@Scope(value = SCOPE_PROTOTYPE)
public class CurrentPersonProvider {
    Person currentPerson;
}
