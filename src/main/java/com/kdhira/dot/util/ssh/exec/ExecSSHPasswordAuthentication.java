package com.kdhira.dot.util.ssh.exec;

/**
 * Authentication implementation for username-password authentication.
 * @author Kevin Hira
 */
public class ExecSSHPasswordAuthentication implements ExecSSHAuthentication {

    protected String password;

    public ExecSSHPasswordAuthentication(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getKeyFile() {
        return null;
    }

}
