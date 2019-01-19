package com.kdhira.dot.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kdhira.dot.job.Job;
import com.kdhira.dot.job.JobManifest;
import com.kdhira.dot.resource.Resource;
import com.kdhira.dot.resource.RootResource;
import com.kdhira.dot.util.yaml.YamlReader;

/**
 * CLI starter for Deployment Orchestration Tool.
 * @author Kevin Hira
 */
public class CLIApplication {

    private Map<String, Resource> resourcePool;
    private List<Job> manifests;
    private YamlReader yamlReader;
    private Settings settings;

    public CLIApplication(Settings settings) {
        this.settings = settings;
        this.resourcePool = new HashMap<String, Resource>();
        this.manifests = new ArrayList<Job>();

        yamlReader = new YamlReader();

        List<String> supportedTypes;

        try {
            supportedTypes = yamlReader.readResource(
                    CLIApplication.class.getClassLoader().getResourceAsStream("supportedTypeDescriptions.yml"));
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


        for (String type : supportedTypes) {
            try {
                yamlReader.registerType(Class.forName(type));
            }
            catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        readResources();
        readManifests();
    }

    public boolean execute() {
        for (Job job : manifests) {
            job.link(resourcePool);
            if (!job.execute()) {
                return false;
            }
        }

        return true;
    }

    private void readResources() {
        List<RootResource> rootResources = new ArrayList<RootResource>();

        for (File resourceFile : settings.getResourceList()) {
            try {
                rootResources.addAll(yamlReader.readDocument(resourceFile, RootResource.class));
            }
            catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        for (RootResource rr : rootResources) {
            for (Resource r : rr.getResources()) {
                if (resourcePool.containsKey(r.getId())) {
                    throw new RuntimeException("Duplicate ids across resources not allowed.");
                }
                resourcePool.put(r.getId(), r);
            }
        }
    }

    private void readManifests() {
        for (File manifestFile : settings.getManifestList()) {
            try {
                manifests.addAll(yamlReader.readDocument(manifestFile, Job.class, JobManifest.class));
            }
            catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
