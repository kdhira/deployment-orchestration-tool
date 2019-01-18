package com.kdhira.dot.schema;

import com.kdhira.dot.resource.Host;
import com.kdhira.dot.resource.Resource;

/**
 * Schema for objects containing {@link Host}.
 * @author Kevin Hira
 */
public interface HostComponentSchema extends Resource {

    Host getConnection();

    void setConnection(Host connection);

}
