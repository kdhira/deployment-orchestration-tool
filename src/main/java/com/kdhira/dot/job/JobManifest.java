package com.kdhira.dot.job;

import java.util.Map;

import com.kdhira.dot.resource.Resource;

public final class JobManifest extends AbstractJob {

    @Override
    public boolean runJob() {
        return true;
    }

    @Override
    protected void linkResources(Map<String, Resource> sharedResources) {

    }

}
