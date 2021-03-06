package com.kdhira.dot.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.kdhira.dot.Constants;
import com.kdhira.dot.resource.Resource;
import com.kdhira.dot.schema.JobManifestSchema;
import com.kdhira.dot.util.ColoredString;
import com.kdhira.dot.util.Resources;
import com.kdhira.dot.util.ColoredString.StringColor;

/**
 * No operation implementation of {@link AbstractJob}.
 * Used as root object in job parsing.
 * @author Kevin Hira
 */
public final class JobManifest extends AbstractJob implements JobManifestSchema {

    private List<Job> subJobs;
    private boolean concurrent;

    public JobManifest() {
        subJobs = new ArrayList<Job>();
    }

    @Override
    public final List<Job> getSubJobs() {
        return subJobs;
    }

    @Override
    public final void setSubJobs(List<Job> subJobs) {
        this.subJobs = subJobs;
    }

    @Override
    public final boolean getConcurrent() {
        return concurrent;
    }

    @Override
    public final void setConcurrent(boolean concurrent) {
        this.concurrent = concurrent;
    }

    @Override
    public boolean runJob() {
        if (concurrent) {
            return executeConcurrently();
        }

        for (Job job : getSubJobs()) {
            println(String.format(Constants.JOB_RUNNING, job.getId()));
            if (!job.execute()) {
                println(new ColoredString(String.format(Constants.JOB_UNSUCCESSFUL, job.getId()), StringColor.RED));
                return false;
            }
            println(new ColoredString(String.format(Constants.JOB_SUCCESSFUL, job.getId()), StringColor.GREEN));
        }
        return true;
    }

    @Override
    protected void linkResources(Map<String, Resource> resourcePool) {
        subJobs = subJobs.stream()
                .map((job) -> Resources.lookup(resourcePool, job))
                .collect(Collectors.toList());

        for (Job job : subJobs) {
            job.setParent(this);
            job.link(resourcePool);
        }
    }

    private boolean executeConcurrently() {
        Map<Job, Boolean> statuses = new ConcurrentHashMap<Job, Boolean>();
        Map<Job, Thread> threads = new HashMap<Job, Thread>();

        for (Job job : getSubJobs()) {
            statuses.put(job, false);
            Thread jobThread = new Thread(() -> {
                println(String.format(Constants.JOB_RUNNING, job.getId()));
                boolean status = job.execute();
                if (status) {
                    println(new ColoredString(String.format(Constants.JOB_SUCCESSFUL, job.getId()), StringColor.GREEN));
                }
                else {
                    println(new ColoredString(String.format(Constants.JOB_UNSUCCESSFUL, job.getId()), StringColor.RED));
                }
                statuses.put(job, status);
            });
            threads.put(job, jobThread);
            jobThread.start();
        }

        while (threads.values().stream().filter((t) -> t.isAlive()).count() > 0);

        return !statuses.containsValue(false);
    }

}
