package com.kdhira.dot.job;

import java.util.Map;

import com.kdhira.dot.resource.Resource;

/**
 * Partial implementation of {@link Job}. Subclasses left to define template methods.
 * @author Kevin Hira
 */
public abstract class AbstractJob implements Job {

    protected String id;
    private String description;
    private Job parent;

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
    public final String getDescription() {
        return description;
    }

    @Override
    public final void setDescription(String description) {
        throwIfNotNull(this.description);
        this.description = description;
    }

    @Override
    public final boolean execute() {
        try {
            validate();
        } catch (JobValidationException e) {
            e.printStackTrace();
            return false;
        }

        println("Running job '" + getId() + "'");
        return runJob();
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
    public final void link(Map<String, Resource> resourcePool) {
        linkResources(resourcePool);
    }

    @Override
    public void validate() throws JobValidationException {

    }

    private void throwIfNotNull(Object obj) {
        if (obj != null) {
            throw new IllegalStateException("Implementation does not allow resetting of this variable");
        }
    }

    protected void println(String s) {
        System.out.println("[" + getFQJI() + "]\t" + s);
    }

    /**
     * Execute the job.
     * @return whether the job ran successfully
     */
    protected abstract boolean runJob();

    /**
     * Link job's resources to shared resource pool.
     * @param resourcePool resource pool to use
     */
    protected abstract void linkResources(Map<String, Resource> resourcePool);

}
