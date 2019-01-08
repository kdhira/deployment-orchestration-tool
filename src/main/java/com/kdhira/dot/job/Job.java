package com.kdhira.dot.job;

public interface Job {

    String getJobId();

    void setJobId(String jobId);

    String getJobDescription();

    void setJobDescription(String jobDescription);

    boolean execute();

    boolean run();

}
