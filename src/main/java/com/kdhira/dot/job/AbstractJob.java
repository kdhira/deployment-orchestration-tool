package com.kdhira.dot.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.kdhira.dot.resource.Resource;
import com.kdhira.dot.util.Resources;

public abstract class AbstractJob implements Job {

    protected String id;
    private String jobDescription;
    private List<Job> subJobs;
    private boolean parallelExecution;
    private Job parent;

    public AbstractJob() {
        subJobs = new ArrayList<Job>();
    }

    @Override
    public final String getId() {
        return id;
    }

    @Override
    public final void setId(String id) {
        throwIfNotNull(this.id);
        this.id = id;
    }

    @Override
    public final String getJobDescription() {
        return jobDescription;
    }

    @Override
    public final void setJobDescription(String jobDescription) {
        throwIfNotNull(this.jobDescription);
        this.jobDescription = jobDescription;
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
    public final boolean getParallelExecution() {
        return parallelExecution;
    }

    @Override
    public final void setParallelExecution(boolean parallelExecution) {
        this.parallelExecution = parallelExecution;
    }

    @Override
    public final boolean execute(boolean parallelExecution) {
        try {
            validate();
        } catch (JobValidationException e) {
            e.printStackTrace();
            return false;
        }

        println("Running job '" + getId() + "'");
        if (!runJob()) {
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

    @Override
    public final Job getParent() {
        return parent;
    }

    @Override
    public final void setParent(Job parent) {
        this.parent = parent;
    }

    @Override
    public String getFQJI() {
        return (getParent() != null ? parent.getFQJI() + "|" : "") + getId(); 
    }

    @Override
    public final void link(Map<String, Resource> sharedResources) {
        this.linkResources(sharedResources);

        subJobs = subJobs.stream()
                .map((job) -> Resources.lookup(sharedResources, job))
                .collect(Collectors.toList());

        for (Job job : subJobs) {
            job.setParent(this);
            job.link(sharedResources);
        }
    }

    @Override
    public void validate() throws JobValidationException {

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

        return !statuses.containsValue(false);
    }

    private void throwIfNotNull(Object obj) {
        if (obj != null) {
            throw new IllegalStateException("Implementation does not allow resetting of this variable");
        }
    }

    protected void println(String s) {
        System.out.println("[" + getFQJI() + "]\t" + s);
    }

    protected abstract boolean runJob();

    protected abstract void linkResources(Map<String, Resource> sharedResources);

}
