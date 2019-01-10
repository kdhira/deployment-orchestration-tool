package com.kdhira.dot.util;

import java.util.Map;

import com.kdhira.dot.resource.Resource;

public class Resources {

    @SuppressWarnings("unchecked")
    public static <T extends Resource> T lookup(Map<String, Resource> resources, T resource) throws ClassCastException {
        if (resource != null) {
            if (resource.getId() != null && !resource.getId().equals("")) {
                if (resources.containsKey(resource.getId())) {
                    return (T)resources.get(resource.getId());
                    
                }
            }
        }
        return resource;
    }

}
