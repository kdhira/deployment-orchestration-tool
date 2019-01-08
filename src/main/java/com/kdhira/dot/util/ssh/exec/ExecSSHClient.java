package com.kdhira.dot.util.ssh.exec;

import java.io.IOException;
import java.io.InputStream;

import com.kdhira.dot.util.ssh.SSHClient;
import com.kdhira.dot.util.ssh.SSHException;

public class ExecSSHClient implements SSHClient {

    private String host;
    private int port;
    private String user;
    private ExecSSHAuthentication auth;

    public ExecSSHClient(String host, String user, ExecSSHAuthentication auth) throws SSHException, IOException {
        this(host, 22, user, auth);
    }

    public ExecSSHClient(String host, int port, String user, ExecSSHAuthentication auth) throws SSHException, IOException {
        this.host = host;
        this.port = port;
        this.user = user;
        this.auth = auth;

        if (execute(":") != 0) {
            throw new SSHException("Could not connect to host");
        }
    }

    @Override
    public int push(String localPath, String remotePath) throws SSHException, IOException {
        StringBuilder commandBuilder = generateBaseCommand("scp");

        commandBuilder
                .append(localPath)
                .append(" ")
                .append(getConnectionString())
                .append(":")
                .append(remotePath);

        return forkProcces(commandBuilder.toString());
    }

    @Override
    public int pull(String remotePath, String localPath) throws SSHException, IOException {
        StringBuilder commandBuilder = generateBaseCommand("scp");

        commandBuilder
                .append(getConnectionString())
                .append(":")
                .append(remotePath)
                .append(" ")
                .append(localPath);

        return forkProcces(commandBuilder.toString());
    }

    @Override
    public int execute(String command) throws SSHException, IOException {
        StringBuilder commandBuilder = generateBaseCommand("ssh");

        commandBuilder
                .append(getConnectionString())
                .append(" ")
                .append(command);

        return forkProcces(commandBuilder.toString());
    }

    private int forkProcces(String command) throws SSHException, IOException {
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("bash", "-c", command);
        pb.redirectErrorStream(true);

        Process p = pb.start();
        InputStream in = p.getInputStream();

        StringBuilder outputBuffer = new StringBuilder();
        while (p.isAlive() || in.available() > 0) {
            try {
                while (in.available() > 0) {
                    int i = in.read();
                    if (i < 0) {
                        break;
                    }
                    if (i == '\n') {
                        System.out.println(outputBuffer.toString());
                        outputBuffer = new StringBuilder();
                    } else {
                        outputBuffer.append((char) i);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return p.exitValue();
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

    private StringBuilder generateBaseCommand(String baseCommand) {
        StringBuilder commandBuilder = new StringBuilder();
        commandBuilder
                .append(passwordInfo())
                .append(baseCommand + " ")
                .append(identityInfo())
                .append(portInfo());

        return commandBuilder;
    }
}
