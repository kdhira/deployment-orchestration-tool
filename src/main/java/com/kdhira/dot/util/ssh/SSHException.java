package com.kdhira.dot.util.ssh;

/**
 * Exception thrown for SSH connection and operation type issues.
 * @author Kevin Hira
 */
public class SSHException extends Exception {

    private static final long serialVersionUID = 1L;

    public SSHException(Throwable throwable) {
        super(throwable);
    }

    public SSHException(String message) {
        super(message);
    }

    public SSHException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
