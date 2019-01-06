package com.kdhira.dot.util.ssh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

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

    public JSchClient(String host, String user, String keyFile) throws SSHException {
        this(host, 22, user, keyFile, null);
    }

    public JSchClient(String host, int port, String user, String keyFile) throws SSHException {
        this(host, port, user, keyFile, null);
    }

    public JSchClient(String host, String user, String keyFile, String keyPass) throws SSHException {
        this(host, 22, user, keyFile, keyPass);
    }

    public JSchClient(String host, int port, String user, String keyFile, String keyPass) throws SSHException {
        this.host = host;
        this.port = port;
        this.user = user;
        jsch = new JSch();

        if (keyFile != null) {
            if (keyPass != null) {
                try {
                    jsch.addIdentity(keyFile, keyPass);
                } catch (JSchException e) {
                    throw new SSHException("Private key is invalid or passphrase incorrect.", e);
                }
            } else {
                try {
                    jsch.addIdentity(keyFile);
                } catch (JSchException e) {
                    throw new SSHException("Private key is invalid.", e);
                }
            }
        }

        try {
            session = jsch.getSession(user, host, port);
        } catch (JSchException e) {
            throw new SSHException("Could not create SSH session. Username and/or host invalid.", e);
        }

        withConfig("StrictHostKeyChecking", "no");
    }

    public SSHClient withConfig(String property, String value) {
        session.setConfig(property, value);
        return this;
    }

    public SSHClient connect() throws SSHException {
        if (!isConnected()) {
            try {
                session.connect();
            } catch (JSchException e) {
                throw new SSHException(e);
            }
        }
        else {
            throw new SSHException("Session already connected.");
        }

        return this;
    }

    public boolean isConnected() {
        return session.isConnected();
    }

    public void disconnect() throws SSHException {
        if (isConnected()) {
            session.disconnect();
        }
        else {
            throw new SSHException("Session not connected.");
        }
    }

    public void push(String localPath, String remotePath) throws SSHException, IOException {
        boolean ptimestamp = true;

        // exec 'scp -t rfile' remotely
        String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + remotePath;
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

        checkAck(in);

        File _lfile = new File(localPath);

        if (ptimestamp) {
            command = "T" + (_lfile.lastModified() / 1000) + " 0";
            // The access time should be sent here,
            // but it is not accessible with JavaAPI ;-<
            command += (" " + (_lfile.lastModified() / 1000) + " 0\n");
            out.write(command.getBytes());
            out.flush();

            checkAck(in);
        }

        // send "C0644 filesize filename", where filename should not include '/'
        long filesize = _lfile.length();
        command = "C0644 " + filesize + " " + _lfile.getName() + "\n";
        out.write(command.getBytes());
        out.flush();

        checkAck(in);

        // send a content of lfile
        FileInputStream fis = new FileInputStream(localPath);
        byte[] buf = new byte[1024];
        // while (true) {
        //     int len = fis.read(buf, 0, buf.length);
        //     if (len <= 0) break;
        //     out.write(buf, 0, len); //out.flush();
        // }

        byte[] data = Files.readAllBytes(_lfile.toPath());
        out.write(data);

        // send '\0'
        out.write(0);
        out.flush();

        checkAck(in);
        out.close();

        try {
            if (fis != null) fis.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        channel.disconnect();
    }


    public void pull(String remotePath, String localPath) throws SSHException, IOException {
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

        while (true) {
            int c = in.read();
            if (c != 'C') {
                break;
            }

            // read '0644 '
            in.read(buf, 0, 5);

            long filesize = 0L;
            while (true) {
                if (in.read(buf, 0, 1) < 0) {
                    // error
                    break;
                }
                if (buf[0] == ' ') break;
                filesize = filesize * 10L + (long) (buf[0] - '0');
            }

            String file = null;
            for (int i = 0; ; i++) {
                in.read(buf, i, 1);
                if (buf[i] == (byte) 0x0a) {
                    file = new String(buf, 0, i);
                    break;
                }
            }

            // send '\0'
            out.write(0);
            out.flush();

            // read a content of lfile
            FileOutputStream fos = new FileOutputStream(prefix == null ? localPath : prefix + file);
            int foo;
            while (true) {
                if (buf.length < filesize) foo = buf.length;
                else foo = (int) filesize;
                foo = in.read(buf, 0, foo);
                if (foo < 0) {
                    // error
                    break;
                }
                fos.write(buf, 0, foo);
                filesize -= foo;
                if (filesize == 0L) break;
            }

            checkAck(in);

            // send '\0'
            out.write(0);
            out.flush();

            try {
                if (fos != null) fos.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }

        channel.disconnect();
    }

    private int readAck(InputStream in) throws IOException {
        int b = in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //         -1
        if (b == 0) return b;
        if (b == -1) return b;

        if (b == 1 || b == 2) {
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            }
            while (c != '\n');
            if (b == 1) { // error
                System.out.print(sb.toString());
            }
            if (b == 2) { // fatal error
                System.out.print(sb.toString());
            }
        }
        return b;
    }

    private void checkAck(InputStream in) throws SSHException, IOException {
        if (readAck(in) != 0) {
            throw new SSHException("Acknowledgement failed.");
        }
    }
}
