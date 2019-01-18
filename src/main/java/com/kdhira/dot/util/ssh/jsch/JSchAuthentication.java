package com.kdhira.dot.util.ssh.jsch;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.kdhira.dot.util.ssh.SSHException;

/**
 * Authentication details holder for SSH sessions.
 * @author Kevin Hira
 */
public interface JSchAuthentication {

    /**
     * Configure a {@link JSch} instance with authentication.
     * @param jsch instance to configure
     * @throws SSHException
     */
    void configureClient(JSch jsch) throws SSHException;

    /**
     * Configure a JSch {@link Session} with this.
     * @param session session to configure
     */
    void configureSession(Session session);

}
