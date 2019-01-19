package com.kdhira.dot.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.kdhira.dot.resource.Resource;
import com.kdhira.dot.schema.JobManifestSchema;
import com.kdhira.dot.util.Resources;

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
            if (!job.execute()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void linkResources(Map<String, Resource> sharedResources) {
        subJobs = subJobs.stream()
                .map((job) -> Resources.lookup(sharedResources, job))
                .collect(Collectors.toList());

        for (Job job : subJobs) {
            job.setParent(this);
            job.link(sharedResources);
        }
    }

    private boolean executeConcurrently() {
        Map<Job, Boolean> statuses = new ConcurrentHashMap<Job, Boolean>();
        Map<Job, Thread> threads = new HashMap<Job, Thread>();

        for (Job job : getSubJobs()) {
            statuses.put(job, false);
            Thread jobThread = new Thread(() -> {
                boolean status = job.execute();
                statuses.put(job, status);
            });
            threads.put(job, jobThread);
            jobThread.start();
        }

        while (threads.values().stream().filter((t) -> t.isAlive()).count() > 0);

        return !statuses.containsValue(false);
    }

}
