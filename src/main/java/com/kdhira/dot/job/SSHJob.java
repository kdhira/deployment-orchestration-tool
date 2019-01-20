package com.kdhira.dot.job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kdhira.dot.resource.Host;
import com.kdhira.dot.resource.Resource;
import com.kdhira.dot.schema.HostComponentSchema;
import com.kdhira.dot.schema.SSHJobSchema;
import com.kdhira.dot.util.Resources;
import com.kdhira.dot.util.ssh.SSHClient;
import com.kdhira.dot.util.ssh.SSHException;
import com.kdhira.dot.util.ssh.SSHRunnable;

/**
 * SSH job implementation. Uses SSH clients to execute commands and transfers to/from remote hosts.
 * @author Kevin Hira
 */
public class SSHJob extends AbstractJob implements SSHJobSchema, HostComponentSchema {

    private List<SSHRunnable> commands;
    private Host connection;

    public SSHJob() {
        commands = new ArrayList<SSHRunnable>();
    }

    @Override
    public boolean runJob() {
        SSHRunnable currentCommand = null;
        try (SSHClient ssh = getConnection().createConnection()) {
            for (SSHRunnable command : commands) {
                currentCommand = command;

                println(command.commandString());
                int exitCode = command.run(ssh, this::println);
                println(command.commandString() + " => Exit code: " + exitCode);

                if (exitCode > 0) {
                    return false;
                }
            }
        }
        catch (SSHException | IOException e) {
            if (currentCommand != null) {
                println(currentCommand.commandString() + " => Error occured!");
            }
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<SSHRunnable> getCommmands() {
        return commands;
    }

    @Override
    public void setCommands(List<SSHRunnable> commands) {
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
    public void validate() throws JobValidationException {
        try {
            getConnection().createConnection();
        } catch (SSHException | IOException | IllegalStateException e) {
            throw new JobValidationException("Connection to host could not be established", e);
        }
    }

    @Override
    protected void linkResources(Map<String, Resource> resourcePool) {
        setConnection(Resources.lookup(resourcePool, getConnection()));
    }

}
