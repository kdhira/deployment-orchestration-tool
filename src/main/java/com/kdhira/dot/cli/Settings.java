package com.kdhira.dot.cli;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.kdhira.dot.util.argument.Validatable;

/**
 * Argument settings for the CLI
 * @author Kevin Hira
 */
public class Settings implements Validatable {

    private List<File> resourceList;
    private List<File> manifestList;

    public Settings() {
        resourceList = new ArrayList<File>();
        manifestList = new ArrayList<File>();
    }

    public List<File> getResourceList() {
        return resourceList;
    }

    public void addResource(File file) {
        resourceList.add(file);
    }

    public List<File> getManifestList() {
        return manifestList;
    }

    public void addManifest(File file) {
        manifestList.add(file);
    }

    @Override
    public boolean validate() {
        if (manifestList.isEmpty()) {
            System.err.println("No manifests to run");
            return false;
        }

        return true;
    }

}
