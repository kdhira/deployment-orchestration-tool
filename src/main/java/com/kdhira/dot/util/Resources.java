package com.kdhira.dot.util;

import java.util.Map;

import com.kdhira.dot.resource.Resource;
/**
 * Static operations regarding {@link Resource} objects.
 * (This might need refactoring)
 * @author Kevin Hira
 */
public class Resources {

    @SuppressWarnings("unchecked")
    /**
     * Lookup a Resource given a pool of resources.
     * @param resources resource pool to consider
     * @param defaultResource resource which id is used for lookup
     * @return resource from pool with matching ids, or defaultResource if none found
     * @throws ClassCastException
     */
    public static <T extends Resource> T lookup(Map<String, Resource> resources, T defaultResource) throws ClassCastException {
        if (defaultResource != null && defaultResource.getId() != null && !defaultResource.getId().equals("")) {
            return (T)resources.getOrDefault(defaultResource.getId(), defaultResource);
        }
        return defaultResource;
    }

}
