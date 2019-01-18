package com.kdhira.dot.schema;

/**
 * Schema for host descriptions.
 * Holds information such as host address, port and login credentials (password or private key).
 * @author Kevin Hira
 */
public interface HostSchema {

    String getHost();

    void setHost(String host);

    int getPort();

    void setPort(int port);

    String getUser();

    void setUser(String user);

    String getPassword();

    void setPassword(String password);

    String getKeyFile();

    void setKeyFile(String keyFile);

    String getKeyPass();

    void setKeyPass(String keyPass);

}
