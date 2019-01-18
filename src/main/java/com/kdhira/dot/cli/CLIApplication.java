package com.kdhira.dot.cli;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kdhira.dot.job.BashJob;
import com.kdhira.dot.job.Job;
import com.kdhira.dot.job.JobManifest;
import com.kdhira.dot.job.SSHJob;
import com.kdhira.dot.resource.JSchHost;
import com.kdhira.dot.resource.Resource;
import com.kdhira.dot.resource.RootResource;
import com.kdhira.dot.resource.SSHHost;
import com.kdhira.dot.util.ssh.SSHCommand;
import com.kdhira.dot.util.ssh.SSHPull;
import com.kdhira.dot.util.ssh.SSHPush;
import com.kdhira.dot.util.yaml.YamlReader;

public class CLIApplication {

    String manifestPath;
    String resourcePath;

    public boolean execute(String[] args) {
        manifestPath = "/Users/kevin/git/kdhira/dot/noop-project/jobConfig.yml";
        resourcePath = "/Users/kevin/git/kdhira/dot/noop-project/resourceConfig.yml";

        YamlReader yamlReader = new YamlReader();
        yamlReader.registerType(BashJob.class);
        yamlReader.registerType(SSHJob.class);
        yamlReader.registerType(JobManifest.class);
        yamlReader.registerType(SSHHost.class);
        yamlReader.registerType(JSchHost.class);
        yamlReader.registerType(SSHCommand.class);
        yamlReader.registerType(SSHPush.class);
        yamlReader.registerType(SSHPull.class);

        Map<String, Resource> resourcePool = new HashMap<String, Resource>();
        try {
            for (RootResource rr : yamlReader.readDocument(resourcePath, RootResource.class)) {
                for (Resource r : rr.getResources()) {
                    if (resourcePool.containsKey(r.getId())) {
                        throw new RuntimeException("Can not have 2 resources with same id.");
                    }
                    resourcePool.put(r.getId(), r);
                }
            }
        }
        catch (FileNotFoundException e) {
            // TODO do better
            e.printStackTrace();
            return false;
        }

        List<Job> manifests;
        try {
            manifests = yamlReader.readDocument(manifestPath, Job.class, JobManifest.class);
        }
        catch (FileNotFoundException e) {
            // TODO do better
            e.printStackTrace();
            return false;
        }

        for (Job job : manifests) {
            job.link(resourcePool);
            job.execute();
        }

        return false;
    }

}
