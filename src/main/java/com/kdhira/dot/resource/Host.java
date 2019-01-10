package com.kdhira.dot.resource;

import java.io.IOException;

import com.kdhira.dot.util.ssh.SSHClient;
import com.kdhira.dot.util.ssh.SSHException;

public interface Host extends Resource {

    SSHClient createConnection() throws SSHException, IOException;

}
