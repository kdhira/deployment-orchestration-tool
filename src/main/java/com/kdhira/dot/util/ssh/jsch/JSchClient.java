package com.kdhira.dot.util.ssh.jsch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.function.Consumer;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.kdhira.dot.util.AlternatingWriter;
import com.kdhira.dot.util.ColoredString;
import com.kdhira.dot.util.ColoredString.StringColor;
import com.kdhira.dot.util.ssh.SSHClient;
import com.kdhira.dot.util.ssh.SSHException;

/**
 * SSH client implementation using JSch (Jcraft).
 * 
 * @author Kevin Hira
 */
public class JSchClient implements SSHClient {

    private static final int BUFFER_SIZE = 1024;

    private JSch jsch;
    private Session session;

    private static Properties defaultProperties() {
        Properties properties = new Properties();
        properties.put("StrictHostKeyChecking", "no");
        return properties;
    }

    public JSchClient(String host, String user, JSchAuthentication auth) throws SSHException {
        this(host, 22, user, auth, defaultProperties());
    }

    public JSchClient(String host, int port, String user, JSchAuthentication auth) throws SSHException {
        this(host, port, user, auth, defaultProperties());
    }

    public JSchClient(String host, String user, JSchAuthentication auth, Properties configuration) throws SSHException {
        this(host, 22, user, auth, configuration);
    }

    public JSchClient(String host, int port, String user, JSchAuthentication auth, Properties configuration)
            throws SSHException {
        jsch = new JSch();

        auth.configureClient(jsch);

        try {
            session = jsch.getSession(user, host, port);
        } catch (JSchException e) {
            throw new SSHException("Could not create SSH session. Username and/or host invalid.", e);
        }

        auth.configureSession(session);
        session.setConfig(configuration);

        try {
            session.connect();
        } catch (JSchException e) {
            throw new SSHException(e);
        }
    }

    @Override
    public void close() throws SSHException {
        if (session.isConnected()) {
            session.disconnect();
        }
    }

    @Override
    public int push(String localPath, String remotePath) throws SSHException, IOException {
        // exec 'scp -t rfile' remotely
        String command = "scp  -t " + remotePath;
        Channel channel = openExecChannel(command);

        // get I/O streams for remote scp
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();

        checkAcknowledgement(in);

        File localFile = new File(localPath);

        // send "C0644 filesize filename", where filename should not include '/'
        long fileSize = localFile.length();
        String fileName = localFile.getName();
        command = "C0644 " + fileSize + " " + fileName + "\n";
        out.write(command.getBytes());
        out.flush();

        checkAcknowledgement(in);

        // send a content of lfile
        FileInputStream fis = new FileInputStream(localFile);
        byte[] buffer = new byte[BUFFER_SIZE];

        int bytesTransferred;
        do {
            bytesTransferred = transferBytes(fis, out, buffer, buffer.length);
        } while (bytesTransferred > 0);

        // send '\0'
        out.write(0);
        out.flush();

        out.close();
        checkAcknowledgement(in);
        in.close();
        fis.close();

        return getExitCodeAndDisconnect(channel);
    }

    @Override
    public int pull(String remotePath, String localPath) throws SSHException, IOException {
        String prefix = null;

        if (new File(localPath).isDirectory()) {
            prefix = localPath + File.separator;
        }

        // exec 'scp -f rfile' remotely
        String command = "scp -f " + remotePath;
        Channel channel = openExecChannel(command);

        // get I/O streams for remote scp
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();

        byte[] buffer = new byte[BUFFER_SIZE];

        // send '\0'
        out.write(0);
        out.flush();

        if (in.read() != 'C') {
            throw new SSHException("Expected start of file metadata, did not receive.");
        }

        // consume '0644 '
        in.read(buffer, 0, 5);

        long fileSize = Long.valueOf(readUntil(in, ' '));
        String fileName = readUntil(in, '\n');

        // send '\0'
        out.write(0);
        out.flush();

        // read a content of lfile
        File localFile = new File(prefix == null ? localPath : prefix + fileName);
        FileOutputStream fos = new FileOutputStream(localFile);
        int bytesTransferred;
        do {
            int nextRead = (buffer.length < fileSize ? buffer.length : (int) fileSize);
            bytesTransferred = transferBytes(in, fos, buffer, nextRead);

            fileSize -= bytesTransferred;
        } while (bytesTransferred > 0 && fileSize > 0L);

        // send '\0'
        out.write(0);
        out.flush();

        out.close();
        checkAcknowledgement(in);
        in.close();
        fos.close();

        return getExitCodeAndDisconnect(channel);
    }

    @Override
    public int execute(String command, Consumer<ColoredString> relay) throws SSHException, IOException {
        Channel channel = openExecChannel(command);
        StringBuilder outputBuffer = new StringBuilder();
        InputStream in = channel.getInputStream();
        InputStream err = channel.getExtInputStream();

        AlternatingWriter alternatingWriter = new AlternatingWriter();
        alternatingWriter.addStream(in, relay, StringColor.CYAN);
        alternatingWriter.addStream(err, relay, StringColor.RED);

        alternatingWriter.relayWhile(channel::isClosed, false);

        System.out.print(outputBuffer.toString());
        return getExitCodeAndDisconnect(channel);

    }

    private Channel openExecChannel(String command) throws SSHException {
        Channel channel;
        try {
            channel = session.openChannel("exec");
        } catch (JSchException e) {
            throw new SSHException("Could not create channel.", e);
        }
        ((ChannelExec) channel).setCommand(command);

        try {
            channel.connect();
        } catch (JSchException e) {
            throw new SSHException("Could not connect to channel.", e);
        }

        return channel;
    }

    private int getExitCodeAndDisconnect(Channel channel) {
        int exitStatus = ((ChannelExec)channel).getExitStatus();
        channel.disconnect();
        return exitStatus;
    }

    private int transferBytes(InputStream in, OutputStream out, byte[] buffer, int length) throws IOException  {
        int bytesRead = in.read(buffer, 0, (buffer.length <= length ? buffer.length : length));
        if (bytesRead > 0) {
            out.write(buffer, 0, bytesRead);
        }
        return bytesRead;
    }

    /**
     * Check aknowledgement from server by reading stream.
     * @param in stream to read
     * @return acknowledgement value
     * may be 0 for success,
     *        1 for error,
     *        2 for fatal error,
     *       -1
     * @throws IOException
     */
    private int readAck(InputStream in) throws IOException {
        int b = in.read();

        if (b > 0) {
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            }
            while (c != '\n');
            throw new IOException(sb.toString());
        }
        return b;
    }

    private String readUntil(InputStream in, char stopper) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c = in.read();
        while (c != stopper) {
            sb.append((char)c);
            c = in.read();
        }
        return sb.toString();
    }

    private void checkAcknowledgement(InputStream in) throws SSHException, IOException {
        if (readAck(in) != 0) {
            throw new SSHException("Acknowledgement failed.");
        }
    }

}
