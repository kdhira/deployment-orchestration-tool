package com.kdhira.dot.job;

public abstract class AbstractJob implements Job {

    protected String jobId;
    private String jobDescription;

    public final String getJobId() {
        return jobId;
    }

    public final void setJobId(String jobId) {
        throwIfNotNull(this.jobId);
        this.jobId = jobId;
    }

    public final String getJobDescription() {
        return jobDescription;
    }

    public final void setJobDescription(String jobDescription) {
        throwIfNotNull(this.jobDescription);
        this.jobDescription = jobDescription;
    }

    private void throwIfNotNull(Object obj) {
        if (obj != null) {
            throw new IllegalStateException("Implementation does not allow resetting of this variable");
        }
    }

}
