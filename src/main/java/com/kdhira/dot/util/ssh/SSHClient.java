package com.kdhira.dot.util.ssh;

import java.io.IOException;

/**
 * Interface for SSH client
 * Uses Jcraft's JSch implementation
 * @author Kevin Hira
 */
public interface SSHClient extends AutoCloseable {

    SSHClient withConfig(String property, String value);

    void push(String localPath, String remotePath) throws SSHException, IOException;

    void pull(String remotePath, String localPath) throws SSHException, IOException;

}
