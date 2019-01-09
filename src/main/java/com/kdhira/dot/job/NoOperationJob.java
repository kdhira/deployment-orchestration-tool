package com.kdhira.dot.job;

import com.kdhira.dot.schema.NoOperationJobSchema;

public final class NoOperationJob extends AbstractJob implements NoOperationJobSchema {

    @Override
    public boolean run() {
        return true;
    }


}
