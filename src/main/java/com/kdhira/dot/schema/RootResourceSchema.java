package com.kdhira.dot.schema;

import java.util.List;

import com.kdhira.dot.resource.Resource;

/**
 * Schema for root resources. Defines the structure of the root object read in as
 * shared resources.
 * @author Kevin Hira
 */
public interface RootResourceSchema {

    List<Resource> getResources();

    void setResources(List<Resource> resources);

}
