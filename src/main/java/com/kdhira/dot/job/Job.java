package com.kdhira.dot.job;

import java.util.List;

public interface Job {

    String getJobId();

    void setJobId(String jobId);

    String getJobDescription();

    void setJobDescription(String jobDescription);

    List<Job> getSubJobs();

    void setSubJobs(List<Job> subjobs);

    boolean getParallelExecution();

    void setParallelExecution(boolean parallelExecution);

    boolean execute(boolean parallelExecution);

    boolean run();

    boolean linkAndValidate();

}
