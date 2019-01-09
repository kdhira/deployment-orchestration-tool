package com.kdhira.dot.util.ssh;

import java.io.IOException;

import com.kdhira.dot.schema.SSHPushPullSchema;

public abstract class AbstractSSHPushPull extends AbstractSSHCommand implements SSHPushPullSchema {

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
