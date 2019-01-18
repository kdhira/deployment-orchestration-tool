package com.kdhira.dot.schema;

/**
 * Schema for command-based SSH operations.
 * @author Kevin Hira
 */
public interface SSHCommandSchema {

    String getCommand();

    void setCommand(String command);

}
