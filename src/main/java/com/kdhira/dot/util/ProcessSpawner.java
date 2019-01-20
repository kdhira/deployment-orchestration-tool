package com.kdhira.dot.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

import com.kdhira.dot.util.ColoredString.StringColor;

/**
 * Class that spawns subprocesses using {@link ProcessBuilder}.
 * @author Kevin Hira
 */
public class ProcessSpawner {

    /**
     * Spawns a process.
     * @param command command to execute
     * @param outputRelay method to consume command output
     * @return exit code of process
     */
    public int spawnProcess(String command, Consumer<ColoredString> outputRelay) {
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("bash", "-c", command);

        try {
            Process p = pb.start();
            InputStream in = p.getInputStream();
            InputStream err = p.getErrorStream();

            AlternatingWriter alternatingWriter = new AlternatingWriter();
            alternatingWriter.addStream(in, outputRelay, StringColor.CYAN);
            alternatingWriter.addStream(err, outputRelay, StringColor.RED);

            alternatingWriter.relayWhile(p::isAlive);

            return p.exitValue();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Spawns a process.
     * @param command command to execute
     * @return exit code of process
     */
    public int spawnProcess(String command) {
        return spawnProcess(command, this::println);
    }

    private void println(ColoredString s) {
        System.out.println("> " + s);
    }

}
