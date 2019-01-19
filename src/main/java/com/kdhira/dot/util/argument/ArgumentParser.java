package com.kdhira.dot.util.argument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Reusable argument parser.
 * @param <T> type of object to configure
 * @author Kevin Hira
 */
public class ArgumentParser<T extends Validatable> {

    private List<String> arguments;
    private List<Rule<T>> rules;

    public ArgumentParser(String[] arguments) {
        this.arguments = new ArrayList<String>(Arrays.asList(arguments));
        this.rules = new ArrayList<Rule<T>>();
    }

    public void addRule(Rule<T> rule) {
        rules.add(rule);
    }

    public T apply(T obj) {
        while (!arguments.isEmpty()) {
            boolean match = false;
            for (Rule<T> rule : rules) {
                if (rule.apply(obj, arguments)) {
                    match = true;
                    break;
                }
            }
            if (!match) {
                throw new IllegalArgumentException("Unexpected argument '" + arguments.get(0) + "'");
            }
        }

        return obj.validate() ? obj : null;
    }

}
