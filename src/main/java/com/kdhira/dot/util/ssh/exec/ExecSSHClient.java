package com.kdhira.dot.util.ssh.exec;

import java.io.IOException;

import com.kdhira.dot.util.ProcessSpawner;
import com.kdhira.dot.util.ssh.SSHClient;
import com.kdhira.dot.util.ssh.SSHException;

/**
 * SSH client implemented using ssh processes.
 * @author Kevin Hira
 */
public class ExecSSHClient implements SSHClient {

    private String host;
    private int port;
    private String user;
    private ExecSSHAuthentication auth;

    private ProcessSpawner processSpawner;

    public ExecSSHClient(String host, String user, ExecSSHAuthentication auth) throws SSHException, IOException {
        this(host, 22, user, auth);
    }

    public ExecSSHClient(String host, int port, String user, ExecSSHAuthentication auth) throws SSHException, IOException {
        this.host = host;
        this.port = port;
        this.user = user;
        this.auth = auth;

        processSpawner = new ProcessSpawner();

        if (execute(":") != 0) {
            throw new SSHException("Could not connect to host");
        }
    }

    @Override
    public int push(String localPath, String remotePath) throws SSHException, IOException {
        String executeCommand = buildCommand(
            generateBaseCommand("scp"),
            localPath,
            " ",
            getConnectionString(),
            ":",
            remotePath
        );

        return forkProcces(executeCommand);
    }

    @Override
    public int pull(String remotePath, String localPath) throws SSHException, IOException {
        String executeCommand = buildCommand(
            generateBaseCommand("scp"),
            getConnectionString(),
            ":",
            remotePath,
            " ",
            localPath
        );

        return forkProcces(executeCommand);
    }

    @Override
    public int execute(String command) throws SSHException, IOException {
        String executeCommand = buildCommand(
            generateBaseCommand("ssh"),
            getConnectionString(),
            " ",
            command
        );

        return forkProcces(executeCommand);
    }

    private int forkProcces(String command) {
        return processSpawner.spawnProcess(command);
    }

    @Override
    public void close() throws SSHException {

    }

    private String getConnectionString() {
        return user + "@" + host;
    }

    private String passwordInfo() {
        StringBuilder commandBuilder = new StringBuilder();
        if (auth.getPassword() != null) {
            commandBuilder
                    .append("sshpass -p '")
                    .append(auth.getPassword())
                    .append("' ");
        }

        return commandBuilder.toString();
    }

    private String identityInfo() {
        StringBuilder commandBuilder = new StringBuilder();
        if (auth.getKeyFile() != null) {
            commandBuilder
                    .append("-i ")
                    .append(auth.getKeyFile())
                    .append(" ");
        }

        return commandBuilder.toString();
    }

    private String portInfo() {
        StringBuilder commandBuilder = new StringBuilder();
        if (port != 22) {
            commandBuilder
                    .append("-p ")
                    .append(port)
                    .append(" ");
        }

        return commandBuilder.toString();
    }

    private String generateBaseCommand(String baseCommand) {
        StringBuilder commandBuilder = new StringBuilder();
        commandBuilder
                .append(passwordInfo())
                .append(baseCommand + " ")
                .append(identityInfo())
                .append(portInfo());

        return commandBuilder.toString();
    }

    private String buildCommand(String... strings) {
        StringBuilder builder = new StringBuilder();

        for (String s : strings) {
            builder.append(s);
        }

        return builder.toString();
    }

}
