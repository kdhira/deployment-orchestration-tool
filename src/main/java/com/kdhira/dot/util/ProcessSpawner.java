package com.kdhira.dot.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

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
    public int spawnProcess(String command, Consumer<String> outputRelay) {
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("bash", "-c", command);
        pb.redirectErrorStream(true);

        try {
            Process p = pb.start();
            InputStream in = p.getInputStream();

            StringBuilder outputBuffer = new StringBuilder();
            while (p.isAlive() || in.available() > 0) {
                while (in.available() > 0) {
                    int i = in.read();
                    if (i < 0) {
                        break;
                    }
                    if (i == '\n') {
                        println(outputBuffer.toString());
                        outputBuffer = new StringBuilder();
                    } else {
                        outputBuffer.append((char) i);
                    }
                }
            }
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

    private void println(String s) {
        System.out.println(s);
    }

}
