package com.kdhira.dot.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kdhira.dot.resource.Resource;
import com.kdhira.dot.schema.BashJobSchema;
import com.kdhira.dot.util.ProcessSpawner;

public class BashJob extends AbstractJob implements BashJobSchema {

    private List<String> commands;
    private List<Integer> allowedExitCodes;

    @Override
    public List<String> getCommands() {
        return commands;
    }

    @Override
    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    @Override
    public boolean run() {
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
    public boolean linkAndValidate() {
        if (!super.linkAndValidate()) {
            return false;
        }
        if (allowedExitCodes == null) {
            allowedExitCodes = new ArrayList<Integer>();
            allowedExitCodes.add(0);
        }
        if (commands == null) {
            commands = new ArrayList<String>();
        }

        return true;
    }

    @Override
    protected void linkResources(Map<String, Resource> sharedResources) {
        
    }

}
