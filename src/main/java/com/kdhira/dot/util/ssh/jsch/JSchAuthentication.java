package com.kdhira.dot.util.ssh.jsch;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.kdhira.dot.util.ssh.SSHException;

public interface JSchAuthentication {

    void configureClient(JSch jsch) throws SSHException;

    void configureSession(Session session);

}
