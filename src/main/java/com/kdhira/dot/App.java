package com.kdhira.dot;

import com.kdhira.dot.cli.CLIApplication;
import com.kdhira.dot.cli.Settings;
import com.kdhira.dot.cli.arguments.ManifestRule;
import com.kdhira.dot.cli.arguments.ResourceRule;
import com.kdhira.dot.util.argument.ArgumentParser;

/**
 * Main entry point for Deployment Orchestration Tool.
 * @author Kevin Hira
 */
public class App {

    public static void main(String[] args) {
        System.out.println("\u001B[36mDeployment Orchestration Tool - Kevin Hira\u001B[0m");

        if (args.length <= 0) {
            System.err.println("No arguments supplied. Exiting");
            System.exit(2);
        }

        ArgumentParser<Settings> argumentParser = new ArgumentParser<Settings>(args);
        argumentParser.addRule(new ManifestRule());
        argumentParser.addRule(new ResourceRule());
        Settings cliSettings = new Settings();
        try {
            cliSettings = argumentParser.apply(new Settings());
        }
        catch (RuntimeException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.err.println(usage());
            System.exit(3);
        }

        if (cliSettings != null) {
            CLIApplication cli = new CLIApplication(cliSettings);
            System.exit(cli.execute() ? 0 : 1);
        }
        else {
            System.err.println(usage());
            System.exit(3);
        }
    }

    private static String usage() {
        StringBuilder builder = new StringBuilder();

        builder.append("\nUsage: dot --manifest|-m <file> [--manifest|-m <file> ...] [--resource|-r <file> ...]");
        builder.append("\n\t--manifest|-m <file>\t\tFile to load job manifests from.");
        builder.append("\n\t--resource|-r <file>\t\tFile to load shared resources from.");

        return builder.toString();
    }

}
