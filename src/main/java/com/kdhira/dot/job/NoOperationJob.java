package com.kdhira.dot.job;

import com.kdhira.dot.schema.NoOperationJobSchema;

public final class NoOperationJob extends AbstractJob implements NoOperationJobSchema {

    @Override
    public boolean run() {
        System.out.println("Job '" + getJobId() + "' => No Operation");
        return true;
    }

    @Override
    public boolean linkAndValidate() {
        return true;
    }

}
