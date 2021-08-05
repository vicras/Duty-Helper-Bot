package com.sbo.service;

import com.sbo.entity.Person;
import org.springframework.stereotype.Service;

/**
 * @author viktar hraskou
 */
public interface AuthorizationService {
    boolean authorize(Class<?> clazz, Person user);
}
