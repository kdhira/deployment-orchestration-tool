package com.kdhira.dot.job;

import java.util.List;
import java.util.Map;

import com.kdhira.dot.resource.Resource;

/**
 * Structural and functional definition for jobs.
 * @author Kevin Hira
 */
public interface Job extends Resource {

    String getJobDescription();

    void setJobDescription(String jobDescription);

    List<Job> getSubJobs();

    void setSubJobs(List<Job> subjobs);

    boolean getParallelExecution();

    void setParallelExecution(boolean parallelExecution);

    Job getParent();

    void setParent(Job parent);

    String getFQJI();

    boolean execute();

    void link(Map<String, Resource> resourcePool);

    void validate() throws JobValidationException;

}
