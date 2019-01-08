package com.kdhira.dot.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSequentialJob extends AbstractJob {

    public final boolean execute() {
        Map<Job, Boolean> statuses = new HashMap<Job, Boolean>();

        if (getSubJobs() == null) {
            setSubJobs(new ArrayList<Job>());
        }

        statuses.put(this, run());

        for (Job job : getSubJobs()) {
            boolean status = job.execute();
            statuses.put(job, status);
        }

        return !statuses.containsValue(new Boolean(false));
    }

}
