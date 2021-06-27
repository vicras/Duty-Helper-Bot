package com.sbo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreationEvent<T> {
    private final T object;
}
