package com.kdhira.dot.job;

import java.util.List;
import java.util.Map;

import com.kdhira.dot.resource.Resource;
import com.kdhira.dot.schema.Identifiable;

public interface Job extends Identifiable {

    String getJobDescription();

    void setJobDescription(String jobDescription);

    List<Job> getSubJobs();

    void setSubJobs(List<Job> subjobs);

    boolean getParallelExecution();

    void setParallelExecution(boolean parallelExecution);

    boolean execute(boolean parallelExecution);

    boolean run();

    boolean linkAndValidate();

    String getFQJI();

    void link(Map<String, Resource> sharedResources);

    void linkParent(Job parentJob);

    void println(String s);

}
