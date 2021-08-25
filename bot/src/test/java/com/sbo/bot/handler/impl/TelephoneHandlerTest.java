package com.sbo.bot.handler.impl;

import com.sbo.bot.handler.impl.settings.TelephoneHandler;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import com.sbo.service.impl.AuthorizationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author viktar hraskou
 */
class TelephoneHandlerTest {

    @Test
    void parseTelNumber() {

        //given
        var authorizationService = Mockito.mock(AuthorizationServiceImpl.class);
        var publisher = Mockito.mock(ApplicationEventPublisher.class);
        var personProvider = Mockito.mock(CurrentPersonProvider.class);
        var personService = Mockito.mock(PersonService.class);

        TelephoneHandler telephoneHandler = new TelephoneHandler(authorizationService, publisher, personProvider, personService);
        var expected = 375297473331L;

        //when
        var tels = new ArrayList<Long>();
        tels.add(telephoneHandler.parseTelNumber("375297473331"));
        tels.add(telephoneHandler.parseTelNumber("+375297473331"));
        tels.add(telephoneHandler.parseTelNumber("+375 (29) 747-33-31"));
        tels.add(telephoneHandler.parseTelNumber(" +375 (29) 747-33-31 "));

        //then
        tels.forEach(tel -> assertEquals(expected, tel));
    }

    @Test
    void parseNotTelNumber() {

        //given
        var authorizationService = Mockito.mock(AuthorizationServiceImpl.class);
        var publisher = Mockito.mock(ApplicationEventPublisher.class);
        var personProvider = Mockito.mock(CurrentPersonProvider.class);
        var personService = Mockito.mock(PersonService.class);

        TelephoneHandler telephoneHandler = new TelephoneHandler(authorizationService, publisher, personProvider, personService);
        var expected = 375297473331L;

        //when
        var tels = List.of(
                "h375297473331",
                "=375297473331",
                "!375297473331",
                "+.375297473331"
        );
        var actual = tels.stream()
                .map(tel -> parseWithException(tel, telephoneHandler))
                .collect(toList());

        //then
        Assertions.assertTrue(actual.stream().allMatch(Objects::isNull));
    }

    private Long parseWithException(String tel, TelephoneHandler telephoneHandler) {
        try {
            return telephoneHandler.parseTelNumber(tel);
        } catch (NumberFormatException ignore) {
            return null;
        }
    }

}