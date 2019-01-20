package com.kdhira.dot.util.ssh;

import java.io.IOException;
import java.util.function.Consumer;

import com.kdhira.dot.util.ColoredString;

/**
 * Implementation for retrieving files from remote hosts.
 * @author Kevin Hira
 */
public class SSHPull extends AbstractSSHPushPull {

    @Override
    public int run(SSHClient client, Consumer<ColoredString> relay) throws SSHException, IOException {
        return client.pull(getRemote(), getLocal());
    }

    @Override
    public String commandString() {
        return "scp: Retrieving '" + getRemote() + "'";
    }

}
