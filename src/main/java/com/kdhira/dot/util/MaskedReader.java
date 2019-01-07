package com.kdhira.dot.util;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

public class MaskedReader {

    public MaskedReader() {
    }

    public String readLine(String prompt) {
        System.out.print(prompt);
        return readLine();
    }

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
