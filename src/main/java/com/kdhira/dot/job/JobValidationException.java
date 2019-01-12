package com.kdhira.dot.job;

public class JobValidationException extends Exception {

    private static final long serialVersionUID = 1L;

    public JobValidationException(Throwable throwable) {
        super(throwable);
    }

    public JobValidationException(String message) {
        super(message);
    }

    public JobValidationException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
