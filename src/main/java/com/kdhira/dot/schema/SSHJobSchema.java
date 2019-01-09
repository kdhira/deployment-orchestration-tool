package com.kdhira.dot.schema;

import java.util.List;

import com.kdhira.dot.resource.Host;
import com.kdhira.dot.util.ssh.AbstractSSHCommand;

public interface SSHJobSchema {

    List<AbstractSSHCommand> getCommmands();

    void setCommands(List<AbstractSSHCommand> sshCommands);

    Host getConnection();

    void setConnection(Host connection);

}
