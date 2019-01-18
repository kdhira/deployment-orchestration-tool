package com.kdhira.dot.util.ssh.exec;

/**
 * Authentication details holder for SSH sessions.
 * @author Kevin Hira
 */
public interface ExecSSHAuthentication {

    /**
     * Get the password configured for username-password authentication.
     * @return user-pass authentication password
     */
    String getPassword();

    /**
     * Get the path to the private key used for private key authentication.
     * @return private key location
     */
    String getKeyFile();

}
