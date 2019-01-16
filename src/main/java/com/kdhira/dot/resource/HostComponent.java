package com.kdhira.dot.resource;

import com.kdhira.dot.resource.Host;

public interface HostComponent extends Resource {

    Host getConnection();

    void setConnection(Host connection);

}
