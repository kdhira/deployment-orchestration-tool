package com.kdhira.dot.schema;

import java.util.List;

import com.kdhira.dot.job.Job;

/**
 * Schema for job manifests.
 * These are jobs that hold other jobs as subjobs.
 * @author Kevin Hira
 */
public interface JobManifestSchema {

    List<Job> getSubJobs();

    void setSubJobs(List<Job> subjobs);

    boolean getConcurrent();

    void setConcurrent(boolean concurrent);

}
