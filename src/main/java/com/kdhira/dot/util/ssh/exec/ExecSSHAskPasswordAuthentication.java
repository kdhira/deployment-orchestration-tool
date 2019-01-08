package com.kdhira.dot.util.ssh.exec;

import com.kdhira.dot.util.MaskedReader;

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
