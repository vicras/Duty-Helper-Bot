package com.sbo.service.impl;

import com.sbo.domain.postgres.entity.Person;
import com.sbo.service.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Checks if user is authorized to use the desired command
 */
@Component
@Slf4j
public class AuthorizationServiceImpl implements AuthorizationService {

    @Override
    public final boolean authorize(Class<?> clazz, Person user) {
        try {
            return checkAuthority(clazz, user);

        } catch (UnsupportedOperationException e) {
            log.error("Attempting to check security on class that is not annotated with @BotCommand: {}",
                    clazz.getSimpleName());
            return true;
        }
    }

    private boolean checkAuthority(Class<?> clazz, Person user) {
        log.debug("Authorizing {} to use {}", user, clazz.getSimpleName());

//        var requiredRoles = List.of(
//                Stream.of(clazz)
//                        .filter(cls -> cls.isAnnotationPresent(BotCommand.class))
//                        .findFirst()
//                        .orElseThrow(UnsupportedOperationException::new)
//                        .getDeclaredAnnotation(BotCommand.class)
//                        .requiredRoles());
//
//        log.debug("User roles: {}\nRequired roles: {}", user.getRoles(), requiredRoles);
//        if (requiredRoles.contains(UNAUTHORIZED)) {
//            return true;
//        }
        return true;
    }
}
