package com.kdhira.dot.util.ssh.exec;

import com.kdhira.dot.util.MaskedReader;

/**
 * Authentication implementation which asks user for password when needed (stdin).
 * @author Kevin Hira
 */
public class ExecSSHAskPasswordAuthentication extends ExecSSHPasswordAuthentication {

    public ExecSSHAskPasswordAuthentication() {
        super(null);
    }

    @Override
    public String getPassword() {
        if (password == null) {
            MaskedReader mr = new MaskedReader();
            password = mr.readLine("Password: ");
        }
        return password;
    }

}
