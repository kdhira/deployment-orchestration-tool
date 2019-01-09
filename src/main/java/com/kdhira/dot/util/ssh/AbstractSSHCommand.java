package com.kdhira.dot.util.ssh;

import java.io.IOException;

public abstract class AbstractSSHCommand {

    public abstract int run(SSHClient client) throws SSHException, IOException;

}
