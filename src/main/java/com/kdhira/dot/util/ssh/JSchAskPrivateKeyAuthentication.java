package com.kdhira.dot.util.ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.kdhira.dot.util.MaskedReader;

public class JSchAskPrivateKeyAuthentication extends JSchPrivateKeyAuthentication {

    public JSchAskPrivateKeyAuthentication(String keyFile) {
        super(keyFile, null);
    }

    @Override
    public void configureClient(JSch jsch) throws SSHException {
        if (keyPass == null) {
            MaskedReader mr = new MaskedReader();
            keyPass = mr.readLine("Passphrase: ");
        }
        super.configureClient(jsch);
    }
}
