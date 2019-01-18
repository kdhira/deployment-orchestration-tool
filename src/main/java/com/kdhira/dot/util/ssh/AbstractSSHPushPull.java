package com.kdhira.dot.util.ssh;

import com.kdhira.dot.schema.SSHPushPullSchema;

/**
 * Schema implementation for SSH push and pull commands.
 * @author Kevin Hira
 */
public abstract class AbstractSSHPushPull implements SSHPushPullSchema, SSHRunnable {

    private String local;
    private String remote;

    @Override
    public String getLocal() {
        return local;
    }

    @Override
    public void setLocal(String local) {
        this.local = local;
    }

    @Override
    public String getRemote() {
        return remote;
    }

    @Override
    public void setRemote(String remote) {
        this.remote = remote;
    }

}
