package com.sbo.service;

import com.sbo.domain.postgres.entity.Person;

/**
 * @author viktar hraskou
 */
public interface AuthorizationService {
    boolean authorize(Class<?> clazz, Person user);
}
