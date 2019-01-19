package com.kdhira.dot.job;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.kdhira.dot.resource.Resource;
import com.kdhira.dot.schema.BashJobSchema;
import com.kdhira.dot.util.ProcessSpawner;

/**
 * Bash job implementation. Spawns subprocesses to execute commands.
 * @author Kevin Hira
 */
public class BashJob extends AbstractJob implements BashJobSchema {

    private final static Integer[] ALLOWED_EXIT_CODES = {0};

    private List<String> commands;
    private List<Integer> allowedExitCodes;

    public BashJob() {
        commands = new ArrayList<String>();
        allowedExitCodes = new ArrayList<Integer>();
        allowedExitCodes.addAll(Arrays.asList(ALLOWED_EXIT_CODES));
    }

    @Override
    public List<String> getCommands() {
        return commands;
    }

    @Override
    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    @Override
    public boolean runJob() {
        ProcessSpawner processSpawner = new ProcessSpawner();
        for (String command : commands) {
            println("Executing `" + command + "`");
            int exitCode = processSpawner.spawnProcess(command);
            println("Command `" + command + "` => Exit code: " + exitCode);

            if (!allowedExitCodes.contains(exitCode)) {
                println("Command `" + command + "` => Exit code: " + exitCode + " not allowed");
                return false;
            }
        }
        return true;
    }

    @Override
    protected void linkResources(Map<String, Resource> resourcePool) {

    }

}
