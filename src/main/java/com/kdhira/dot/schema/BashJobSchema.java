package com.kdhira.dot.schema;

import java.util.List;

/**
 * Schema for Bash jobs.
 * Commands are run as subprocesses.
 * @author Kevin Hira
 */
public interface BashJobSchema {

    List<String> getCommands();

    void setCommands(List<String> commands);

}
