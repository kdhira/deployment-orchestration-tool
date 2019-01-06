package com.kdhira.dot.util.ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public interface JSchAuthentication {

    void configureClient(JSch jsch) throws SSHException;

    void configureSession(Session session);
}
