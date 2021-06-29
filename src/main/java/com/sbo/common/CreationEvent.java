package com.sbo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor

public class CreationEvent<T> implements Serializable {
    private final T object;
}
