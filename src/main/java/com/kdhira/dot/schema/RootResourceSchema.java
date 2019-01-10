package com.kdhira.dot.schema;

import java.util.List;

import com.kdhira.dot.resource.Resource;

public interface RootResourceSchema {

    List<Resource> getResources();

    void setResources(List<Resource> resources);

}
