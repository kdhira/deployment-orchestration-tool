package com.kdhira.dot.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractSequentialJob extends AbstractJob {

    private List<Job> subJobs;

    public final boolean execute() {
        Map<Job, Boolean> statuses = new HashMap<Job, Boolean>();

        statuses.put(this, run());

        for (Job job : subJobs) {
            boolean status = job.execute();
            statuses.put(job, status);
        }

        return !statuses.containsValue(new Boolean(false));
    }

}
