package com.kdhira.dot.util.ssh;

import java.io.IOException;

import com.kdhira.dot.schema.SSHCommandSchema;

public class SSHCommand extends AbstractSSHCommand implements SSHCommandSchema {

    private String command;

    @Override
    public int run(SSHClient client) throws SSHException, IOException {
        return client.execute(command);
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public void setCommand(String command) {
        this.command = command;
    }

}
