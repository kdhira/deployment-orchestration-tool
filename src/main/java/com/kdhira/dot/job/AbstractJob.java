package com.kdhira.dot.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.kdhira.dot.resource.Resource;

public abstract class AbstractJob implements Job {

    protected String id;
    private String jobDescription;
    private List<Job> subJobs;
    private boolean parallelExecution;
    private Job parentJob;

    public AbstractJob() {
        subJobs = new ArrayList<Job>();
    }

    public final String getId() {
        return id;
    }

    public final void setId(String id) {
        throwIfNotNull(this.id);
        this.id = id;
    }

    public final String getJobDescription() {
        return jobDescription;
    }

    public final void setJobDescription(String jobDescription) {
        throwIfNotNull(this.jobDescription);
        this.jobDescription = jobDescription;
    }

    public final List<Job> getSubJobs() {
        return subJobs;
    }

    public final void setSubJobs(List<Job> subJobs) {
        this.subJobs = subJobs;
    }

    public final boolean getParallelExecution() {
        return parallelExecution;
    }

    public final void setParallelExecution(boolean parallelExecution) {
        this.parallelExecution = parallelExecution;
    }

    private void throwIfNotNull(Object obj) {
        if (obj != null) {
            throw new IllegalStateException("Implementation does not allow resetting of this variable");
        }
    }

    public final boolean execute(boolean parallelExecution) {
        if (!linkAndValidate()) {
            throw new JobValidationException("Failed to link and validate job");
        }

        println("Running job '" + getId() + "'");
        if (!run()) {
            return false;
        }

        if (parallelExecution) {
            return executeInParallel();
        }

        for (Job job : getSubJobs()) {
            if (!job.execute(job.getParallelExecution())) {
                return false;
            }
        }

        return true;
    }

    private final boolean executeInParallel() {
        Map<Job, Boolean> statuses = new ConcurrentHashMap<Job, Boolean>();
        Map<Job, Thread> threads = new HashMap<Job, Thread>();

        for (Job job : getSubJobs()) {
            statuses.put(job, false);
            Thread jobThread = new Thread(() -> {
                boolean status = job.execute(job.getParallelExecution());
                statuses.put(job, status);
            });
            threads.put(job, jobThread);
            jobThread.start();
        }

        while (threads.values().stream().filter((t) -> t.isAlive()).count() > 0);

        return !statuses.containsValue(new Boolean(false));
    }

    public boolean linkAndValidate() {
        if (subJobs == null) {
            this.subJobs = new ArrayList<Job>();
        }

        for (Job job : subJobs) {
            job.linkParent(this);
        }

        return true;
    }

    public Job getParent() {
        return parentJob;
    }

    public void linkParent(Job parentJob) {
        this.parentJob = parentJob;
    }

    public String getFQJI() {
        return (getParent() != null ? parentJob.getFQJI() + "|" : "") + getId(); 
    }

    public void link(Map<String, Resource> sharedResources) {
        this.linkResources(sharedResources);

        for (Job job : subJobs) {
            job.link(sharedResources);
        }
    }

    protected abstract void linkResources(Map<String, Resource> sharedResources);

    public void println(String s) {
        System.out.println("[" + getFQJI() + "]\t" + s);
    }

}
