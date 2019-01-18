package com.kdhira.dot.job;

/**
 * Exception that should be thrown under any job validation issue.
 * @author Kevin Hira
 * @see Job#validate()
 */
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
