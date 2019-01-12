package com.kdhira.dot.job;

import java.util.Map;

import com.kdhira.dot.resource.Resource;
import com.kdhira.dot.schema.NoOperationJobSchema;

public final class JobManifest extends AbstractJob implements NoOperationJobSchema {

    @Override
    public boolean runJob() {
        return true;
    }

    @Override
    protected void linkResources(Map<String, Resource> sharedResources) {

    }

}
