package com.kdhira.dot.util.ssh;

import java.io.IOException;
import java.util.function.Consumer;

import com.kdhira.dot.schema.SSHCommandSchema;
import com.kdhira.dot.util.ColoredString;

/**
 * Implementation for running SSH commands remotely.
 * @author Kevin Hira
 */
public class SSHCommand implements SSHCommandSchema, SSHRunnable {

    private String command;

    @Override
    public int run(SSHClient client, Consumer<ColoredString> relay) throws SSHException, IOException {
        return client.execute(command, relay);
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String commandString() {
        return "ssh: " + getCommand();
    }

}
