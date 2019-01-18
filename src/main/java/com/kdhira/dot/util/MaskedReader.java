package com.kdhira.dot.util;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Console reader which masks input.
 * In the case the console can not be distinguished, falls back to using stdin.
 * @author Kevin Hira
 */
public class MaskedReader {

    /**
     * Reads line and masks input, if possible.
     * @param prompt message to give
     * @return text that was entered
     */
    public String readLine(String prompt) {
        System.out.print(prompt);
        return readLine();
    }

    /**
     * Reads line and masks input, if possible
     * @return text that was entered
     */
    public String readLine() {
        Console console = System.console();
        if (console != null) {
            return new String(console.readPassword());
        }
        try {
            return new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
