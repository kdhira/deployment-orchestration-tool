package com.kdhira.dot.job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kdhira.dot.resource.Host;
import com.kdhira.dot.resource.Resource;
import com.kdhira.dot.schema.SSHJobSchema;
import com.kdhira.dot.util.Resources;
import com.kdhira.dot.util.ssh.AbstractSSHCommand;
import com.kdhira.dot.util.ssh.SSHClient;
import com.kdhira.dot.util.ssh.SSHException;

public class SSHJob extends AbstractJob implements SSHJobSchema {

    private List<AbstractSSHCommand> commands;
    private Host connection;

    @Override
    public boolean run() {
        try (SSHClient ssh = getConnection().createConnection()) {
            for (AbstractSSHCommand command : commands) {
                command.run(ssh);
            }
        }
        catch (SSHException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<AbstractSSHCommand> getCommmands() {
        return commands;
    }

    @Override
    public void setCommands(List<AbstractSSHCommand> commands) {
        this.commands = commands;
    }

    @Override
    public Host getConnection() {
        return connection;
    }

    @Override
    public void setConnection(Host connection) {
        this.connection = connection;
    }

    @Override
    public boolean linkAndValidate() {
        if (!super.linkAndValidate()) {
            return false;
        }
        if (commands == null) {
            commands = new ArrayList<AbstractSSHCommand>();
        }

        try {
            getConnection().createConnection();
        } catch (SSHException | IOException | IllegalStateException e) {
            throw new JobValidationException("Connection to host could not be established", e);
        }

        return true;
    }

    @Override
    protected void linkResources(Map<String, Resource> sharedResources) {
        setConnection(Resources.lookup(sharedResources, getConnection()));
    }

}
