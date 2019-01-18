package com.kdhira.dot.schema;

/**
 * Schema for transfer based SSH commands.
 * @author Kevin Hira
 */
public interface SSHPushPullSchema {

    String getLocal();

    void setLocal(String local);

    String getRemote();

    void setRemote(String remote);

}
