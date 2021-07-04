package com.sbo.bot.handler.impl;

import com.sbo.bot.security.AuthorizationService;
import com.sbo.provider.CurrentPersonProvider;
import com.sbo.service.PersonService;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author viktar hraskou
 */
class EmailHandlerTest {
// TODO negative result test

    @Test
    void parseEmail(){

        ///given
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getText())
                .thenReturn("message@gmail.com");
        Mockito.when(update.getMessage())
                .thenReturn(message);

        var authorizationService = Mockito.mock(AuthorizationService.class);
        var publisher = Mockito.mock(ApplicationEventPublisher.class);
        var personProvider = Mockito.mock(CurrentPersonProvider.class);
        var personService = Mockito.mock(PersonService.class);
        EmailHandler emailHandler = new EmailHandler(authorizationService, publisher,personProvider, new EmailValidator(), personService);

        //when
        boolean condition = emailHandler.canProcessMessage(update);

        //then
        assertTrue(condition);

    }
}