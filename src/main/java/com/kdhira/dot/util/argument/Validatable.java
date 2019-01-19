package com.kdhira.dot.util.argument;

/**
 * Functional interface for something that can be validated.
 * @author Kevin Hira
 */
@FunctionalInterface
public interface Validatable {

    boolean validate();

}
