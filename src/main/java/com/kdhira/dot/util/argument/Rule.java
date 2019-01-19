package com.kdhira.dot.util.argument;

import java.util.List;

@FunctionalInterface
public interface Rule<T> {

    boolean apply(T subject, List<String> arguments);

}
