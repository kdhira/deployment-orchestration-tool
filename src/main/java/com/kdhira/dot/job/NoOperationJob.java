package com.kdhira.dot.job;

import java.util.Map;

import com.kdhira.dot.resource.Resource;
import com.kdhira.dot.schema.NoOperationJobSchema;

public final class NoOperationJob extends AbstractJob implements NoOperationJobSchema {

    @Override
    public boolean run() {
        return true;
    }

    @Override
    protected void linkResources(Map<String, Resource> sharedResources) {
        
    }


}
