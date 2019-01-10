package com.kdhira.dot.resource;

import com.kdhira.dot.resource.Host;

public interface HostResource extends Resource {

    Host getConnection();

    void setConnection(Host connection);

}
