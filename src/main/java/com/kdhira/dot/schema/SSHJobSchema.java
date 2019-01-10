package com.kdhira.dot.schema;

import java.util.List;

import com.kdhira.dot.resource.HostResource;
import com.kdhira.dot.util.ssh.AbstractSSHCommand;

public interface SSHJobSchema extends HostResource {

    List<AbstractSSHCommand> getCommmands();

    void setCommands(List<AbstractSSHCommand> sshCommands);

}
