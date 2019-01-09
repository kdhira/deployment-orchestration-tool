package com.kdhira.dot.util;

import java.io.IOException;
import java.io.InputStream;

public class ProcessSpawner {

    public int spawnProcess(String command) {
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

    private void println(String s) {
        System.out.println(s);

    }

}
