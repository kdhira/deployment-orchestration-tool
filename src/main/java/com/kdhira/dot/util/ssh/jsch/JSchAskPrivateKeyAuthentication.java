package com.kdhira.dot.util.ssh.jsch;

import com.jcraft.jsch.JSch;
import com.kdhira.dot.util.MaskedReader;
import com.kdhira.dot.util.ssh.SSHException;

/**
 * Authentication implementation which sets up private key authentication, asking for passphrase.
 * @author Kevin Hira
 */
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
