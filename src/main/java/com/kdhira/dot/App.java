/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.kdhira.dot;

import com.kdhira.dot.cli.CLIApplication;

public class App {
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());

        if (args.length > 0) {
            new CLIApplication().execute(args);
        }
    }
}
