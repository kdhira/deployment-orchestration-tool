package com.kdhira.dot.util.ssh;

import java.io.IOException;

public class SSHPush extends AbstractSSHPushPull {

    @Override
    public int run(SSHClient client) throws SSHException, IOException {
        return client.push(getLocal(), getRemote());
    }

}
