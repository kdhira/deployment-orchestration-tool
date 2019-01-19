package com.kdhira.dot.util.ssh;

import java.io.IOException;
import java.util.function.Consumer;

import com.kdhira.dot.util.ColoredString;

/**
 * Implementation for sending files to remote hosts.
 * @author Kevin Hira
 */
public class SSHPush extends AbstractSSHPushPull {

    @Override
    public int run(SSHClient client, Consumer<ColoredString> relay) throws SSHException, IOException {
        return client.push(getLocal(), getRemote());
    }

    @Override
    public String commandString() {
        return "scp: Sending '" + getLocal() + "'";
    }

}
