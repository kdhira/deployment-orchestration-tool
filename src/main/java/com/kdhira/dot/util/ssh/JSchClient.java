package com.kdhira.dot.util.ssh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class JSchClient implements SSHClient {

    private JSch jsch;
    private Session session;

    private String host;
    private int port;
    private String user;

    public JSchClient(String host, String user, JSchAuthentication auth) throws SSHException {
        this(host, 22, user, auth, new Properties());
    }

    public JSchClient(String host, int port, String user, JSchAuthentication auth) throws SSHException {
        this(host, port, user, auth, new Properties());
    }

    public JSchClient(String host, String user, JSchAuthentication auth, Properties configuration) throws SSHException {
        this(host, 22, user, auth, configuration);
    }
    public JSchClient(String host, int port, String user, JSchAuthentication auth, Properties configuration) throws SSHException {
        this.host = host;
        this.port = port;
        this.user = user;
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

    public int push(String localPath, String remotePath) throws SSHException, IOException {
        // exec 'scp -t rfile' remotely
        String command = "scp  -t " + remotePath;
        Channel channel;
        try {
            channel = session.openChannel("exec");
        } catch (JSchException e) {
            throw new SSHException("Could not create channel.", e);
        }
        ((ChannelExec) channel).setCommand(command);

        // get I/O streams for remote scp
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();

        try {
            channel.connect();
        } catch (JSchException e) {
            throw new SSHException("Could not connect to channel.", e);
        }

        checkAcknowledgement(in);

        File localFile = new File(localPath);

        // send "C0644 filesize filename", where filename should not include '/'
        long filesize = localFile.length();
        command = "C0644 " + filesize + " " + localFile.getName() + "\n";
        out.write(command.getBytes());
        out.flush();

        checkAcknowledgement(in);

        // send a content of lfile
        FileInputStream fis = new FileInputStream(localFile);
        byte[] buf = new byte[1024];


        long transfered = 0;
        String lastMessage = "";


        while (true) {
            int bytesTransferred = transferBytes(fis, out, buf, buf.length);
            if (bytesTransferred <= 0) {
                System.out.println();
                break;
            }


            transfered += bytesTransferred;
            String next = (transfered * 100 / filesize) + "%";
            eraseAndWrite(lastMessage, next);
            lastMessage = next;
        }

        

        // send '\0'
        out.write(0);
        out.flush();


        out.close();
        checkAcknowledgement(in);
        in.close();
        fis.close();

        return getExitCodeAndDisconnect(channel);
    }

    private int transferBytes(InputStream in, OutputStream out, byte[] buffer, int length) throws IOException  {
        int bytesRead = in.read(buffer, 0, (buffer.length <= length ? buffer.length : length));
        if (bytesRead > 0) {
            out.write(buffer, 0, bytesRead);
        }
        return bytesRead;
    }

    public int pull(String remotePath, String localPath) throws SSHException, IOException {
        String prefix = null;

        if (new File(localPath).isDirectory()) {
            prefix = localPath + File.separator;
        }

        // exec 'scp -f rfile' remotely
        String command = "scp -f " + remotePath;
        Channel channel;
        try {
            channel = session.openChannel("exec");
        } catch (JSchException e) {
            throw new SSHException("Could not create channel.", e);
        }
        ((ChannelExec) channel).setCommand(command);

        // get I/O streams for remote scp
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();

        try {
            channel.connect();
        } catch (JSchException e) {
            throw new SSHException("Could not connect to channel.", e);
        }

        byte[] buf = new byte[1024];

        // send '\0'
        out.write(0);
        out.flush();

       
        int c = in.read();
        if (c != 'C') {
            throw new SSHException("Expected start of file metadata, did not receive.");
        }

        // consume '0644 '
        in.read(buf, 0, 5);

        long filesize = Long.valueOf(readUntil(in, ' '));
        String fileName = readUntil(in, '\n');

        // send '\0'
        out.write(0);
        out.flush();

        // read a content of lfile
        FileOutputStream fos = new FileOutputStream(prefix == null ? localPath : prefix + fileName);
        while (true) {
            int nextRead = (buf.length < filesize ? buf.length : (int)filesize);
            int bytesTransferred = transferBytes(in, fos, buf, nextRead);
            if (bytesTransferred <= 0) {
                break;
            }

            filesize -= bytesTransferred;
            if (filesize == 0L) {
                break;
            }
        }

        // checkAcknowledgement(in);

        // send '\0'
        out.write(0);
        out.flush();

        out.close();
        checkAcknowledgement(in);
        in.close();
        fos.close();

        return getExitCodeAndDisconnect(channel);
    }

    private int getExitCodeAndDisconnect(Channel channel) {
        int exitStatus = channel.getExitStatus();
        channel.disconnect();
        return exitStatus;
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

    private void eraseAndWrite(String last, String next) {
        StringBuilder sb = new StringBuilder();
        for (char i : last.toCharArray()) {
            sb.append("\010");
        }
        System.out.print(sb.toString());
        System.out.print(next);
    }
}
