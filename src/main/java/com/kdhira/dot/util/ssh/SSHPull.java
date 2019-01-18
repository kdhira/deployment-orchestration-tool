package com.kdhira.dot.util.ssh;

import java.io.IOException;

/**
 * Implementation for retrieving files from remote hosts.
 * @author Kevin Hira
 */
public class SSHPull extends AbstractSSHPushPull {

    @Override
    public int run(SSHClient client) throws SSHException, IOException {
        return client.pull(getRemote(), getLocal());
    }

}
