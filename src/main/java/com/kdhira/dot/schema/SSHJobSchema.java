package com.kdhira.dot.schema;

import java.util.List;

import com.kdhira.dot.util.ssh.SSHRunnable;

/**
 * Schema for SSH jobs.
 * @author Kevin Hira
 */
public interface SSHJobSchema {

    List<SSHRunnable> getCommmands();

    void setCommands(List<SSHRunnable> sshCommands);

}
