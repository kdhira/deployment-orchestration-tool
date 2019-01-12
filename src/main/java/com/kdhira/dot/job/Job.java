package com.kdhira.dot.job;

import java.util.List;
import java.util.Map;

import com.kdhira.dot.resource.Resource;

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

    boolean execute(boolean parallelExecution);

    void link(Map<String, Resource> sharedResources);

    void validate() throws JobValidationException;



}
