package com.kdhira.dot.resource;

import java.util.ArrayList;
import java.util.List;

import com.kdhira.dot.schema.RootResourceSchema;

public class RootResource implements RootResourceSchema {

    private List<Resource> resources;

    public RootResource() {
        resources = new ArrayList<Resource>();
    }

    @Override
    public List<Resource> getResources() {
        return resources;
    }

    @Override
    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

}
