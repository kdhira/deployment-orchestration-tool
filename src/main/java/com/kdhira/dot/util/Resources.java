package com.kdhira.dot.util;

import java.util.Map;

import com.kdhira.dot.resource.Resource;

public class Resources {

    @SuppressWarnings("unchecked")
    public static <T extends Resource> T lookup(Map<String, Resource> resources, T defaultResource) throws ClassCastException {
        if (defaultResource != null && defaultResource.getId() != null && !defaultResource.getId().equals("")) {
            return (T)resources.getOrDefault(defaultResource.getId(), defaultResource);
        }
        return defaultResource;
    }

}
