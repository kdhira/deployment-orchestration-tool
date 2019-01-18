package com.kdhira.dot.job;

import java.util.Map;

import com.kdhira.dot.resource.Resource;

/**
 * No operation implementation of {@link AbstractJob}.
 * Used as root object in job parsing.
 * @author Kevin Hira
 */
public final class JobManifest extends AbstractJob {

    @Override
    public boolean runJob() {
        return true;
    }

    @Override
    protected void linkResources(Map<String, Resource> resourcePool) {

    }

}
