package com.kdhira.dot.util.argument;

import java.util.List;

/**
 * Functional interface for rule based parsing.
 * @param <T> type of object to perform action on
 * @author Kevin Hira
 */
@FunctionalInterface
public interface Rule<T> {

    boolean apply(T subject, List<String> arguments);

}
