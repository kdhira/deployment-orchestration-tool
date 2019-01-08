package com.kdhira.dot.job;

import com.kdhira.dot.schema.NoOperationJobSchema;

public final class NoOperationJob extends AbstractSequentialJob implements NoOperationJobSchema {

    @Override
    public boolean run() {
        System.out.println("Job '" + getJobId() + "' => No Operation");
        return true;
    }

}
