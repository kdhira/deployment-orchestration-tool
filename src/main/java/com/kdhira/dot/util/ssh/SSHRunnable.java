package com.kdhira.dot.util.ssh;

import java.io.IOException;
import java.util.function.Consumer;

import com.kdhira.dot.util.ColoredString;

/**
 * Functional definition for SSH operations.
 * @author Kevin Hira
 */
public interface SSHRunnable {

    int run(SSHClient client, Consumer<ColoredString> relay) throws SSHException, IOException;

    String commandString();

}
