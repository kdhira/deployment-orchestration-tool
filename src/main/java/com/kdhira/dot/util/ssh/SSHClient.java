package com.kdhira.dot.util.ssh;

import java.io.IOException;
import java.util.function.Consumer;

import com.kdhira.dot.util.ColoredString;

/**
 * Interface for SSH clients.
 * @author Kevin Hira
 */
public interface SSHClient extends AutoCloseable {

    /**
     * Copy a local file to the remote host.
     * @param localPath path to local file.
     * @param remotePath path on remote to copy to.
     * @return exit code of the operation
     * @throws SSHException
     * @throws IOException
     */
    int push(String localPath, String remotePath) throws SSHException, IOException;

    /**
     * Copy a remote file to the local machine.
     * @param remotePath path of the remote file.
     * @param localPath path on local to copy to.
     * @return exit code of the operation
     * @throws SSHException
     * @throws IOException
     */
    int pull(String remotePath, String localPath) throws SSHException, IOException;

    /**
     * Execute a command on the remote host.
     * @param command the command to execute.
     * @return exit code of the operation.
     * @throws SSHException
     * @throws IOException
     */
    int execute(String command, Consumer<ColoredString> relay) throws SSHException, IOException;

    void close() throws SSHException;

}
