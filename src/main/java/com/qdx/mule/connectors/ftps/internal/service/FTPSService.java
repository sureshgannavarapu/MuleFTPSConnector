package com.qdx.mule.connectors.ftps.internal.service;

import com.qdx.mule.connectors.ftps.internal.domain.RemoteFile;
import com.qdx.mule.connectors.ftps.internal.domain.RemoteFileType;
import com.qdx.mule.connectors.ftps.internal.errors.FtpsException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FTPSService {

    private static final Logger LOGGER = LogManager.getLogger(FTPSService.class);

    public FTPSService() {
    }

    /**
     *
     * Test for dates received from server (if in difference timezones). We may have to set server
     * configuration.
     *
     * @param client
     * @param host
     * @param port
     * @param user
     * @param password
     * @return
     */
    public boolean connect(FTPSClient client, String host, Integer port, String user, String password) {
        LOGGER.debug(">> connect({},{},{})", host, port, user);
        try {
            boolean connected = false;
            client.connect(host, port);
            client.setFileType(FTP.BINARY_FILE_TYPE);
            client.execPBSZ(0);  // Set protection buffer size
            client.execPROT("P"); // Set data channel protection to private
            client.enterLocalPassiveMode();
            connected = client.login(user, password);

            LOGGER.debug("<< connect(): {}", connected);
            return connected;
        } catch (IOException ex) {
            throw new FtpsException("Error when connecting to FTPS server", ex);
        }
    }

    public boolean disconnect(FTPSClient client) throws IOException {
        LOGGER.debug(">> disconnect()");
        if (client.isConnected()) {
            client.logout();
            client.disconnect();

            LOGGER.debug("<< disconnect(): {}", true);
            return true;
        }

        return false;
    }

    public boolean duplicate(FTPSClient client, String fileName, String sourceDirectory, String destinationDirectory) throws IOException {
        LOGGER.debug(">> copy({},{},{})", fileName, sourceDirectory, destinationDirectory);
        boolean copied = false;
        try {
            String sourceFilePath = String.format("%s/%s", sourceDirectory, fileName);
            ByteArrayOutputStream fileOutputStream = new ByteArrayOutputStream();
            client.retrieveFile(sourceFilePath, fileOutputStream);
            InputStream fileInputStream = new ByteArrayInputStream(fileOutputStream.toByteArray());
            boolean changed = client.changeWorkingDirectory(destinationDirectory);
            if (changed) {
                copied = client.storeFile(fileName, fileInputStream);
            }

            LOGGER.debug("<< copy(): {}", copied);
            return copied;
        } catch (IOException ex) {
            throw new FtpsException("Error when copying file", ex);
        }
    }

    public boolean createDirectory(FTPSClient client, String remoteParentPath, String dirName) throws IOException {
        LOGGER.debug(">> createDirectory({}, {})", remoteParentPath, dirName);
        boolean created = false;
        boolean changed = client.changeWorkingDirectory(remoteParentPath);
        if (changed) {
            created = client.makeDirectory(dirName);
        } else {
            throw new FtpsException("Could not change to parent path");
        }

        LOGGER.debug("createDirectory(): {}", created);
        return created;
    }

    public boolean deleteFile(FTPSClient client, String filePath) throws IOException {
        LOGGER.debug(">> deleteFile({})", filePath);
        boolean success = client.deleteFile(filePath);
        LOGGER.debug("<< deleteFile(): {}", success);
        return success;
    }

    public boolean deleteDirectory(FTPSClient client, String directoryPath) throws IOException {
        LOGGER.debug(">> deleteDirectory({})", directoryPath);
        try {
            boolean success = client.removeDirectory(directoryPath);
            LOGGER.debug("<< deleteDirectory(): {}", success);
            return success;
        } catch (IOException ex) {
            throw new FtpsException(ex);
        }
    }

    public List<RemoteFile> list(FTPSClient client, String remotePath) throws IOException {
        LOGGER.debug(">> list({})", remotePath);
        List<RemoteFile> remoteFiles = new ArrayList<>();
        remoteFiles.addAll(listDirectories(client, remotePath));
        remoteFiles.addAll(listFiles(client, remotePath));
        LOGGER.debug("<< list(): {}", remoteFiles.size());
        return remoteFiles;
    }

    public List<RemoteFile> listFiles(FTPSClient client, String remotePath) throws IOException {
        LOGGER.debug(">> listFiles({})", remotePath);
        client.setFileType(FTP.BINARY_FILE_TYPE);
        client.execPBSZ(0);  // Set protection buffer size
        client.execPROT("P"); // Set data channel protection to private
        client.enterLocalPassiveMode();

        FTPFile[] files = client.listFiles(remotePath);
        List<RemoteFile> fileNames = new ArrayList<>();
        for (FTPFile file : files) {
            if (file.isFile()) {
                fileNames.add(extractFileMetadata(file));
            }
        }

        LOGGER.debug("<< listFiles(): {}", fileNames.size());
        return fileNames;
    }

    public List<RemoteFile> listDirectories(FTPSClient client, String remotePath) throws IOException {
        LOGGER.debug(">> listDirectories({})", remotePath);
        client.setFileType(FTP.BINARY_FILE_TYPE);
        client.execPBSZ(0);  // Set protection buffer size
        client.execPROT("P"); // Set data channel protection to private
        client.enterLocalPassiveMode();
        FTPFile[] files = client.listFiles(remotePath);
        List<RemoteFile> fileNames = new ArrayList<>();
        for (FTPFile file : files) {
            if (file.isDirectory()) {
                fileNames.add(extractFileMetadata(file));
            }
        }

        LOGGER.debug("<< listDirectories(): {}", fileNames.size());
        return fileNames;
    }

    public boolean copyFileToRemote(FTPSClient client, String remotePath, String fileName, InputStream fis) throws IOException {
        LOGGER.debug(">> copyFileToRemote({}, {})", remotePath, fileName);
        String path = String.format("%s/%s", remotePath, fileName);
        boolean success = client.storeFile(path, fis);
        LOGGER.debug("<< copyFileToRemote(): {}", success);
        return success;
    }

    public File copyFileToLocal(FTPSClient client, String remotePath, String fileName) throws IOException {
        LOGGER.debug(">> copyFileToLocal({}, {})", remotePath, fileName);
        File file = new File(fileName);
        FileOutputStream fos = new FileOutputStream(file);
        boolean copied = client.retrieveFile(String.format("%s/%s", remotePath, fileName), fos);
        LOGGER.debug("<< copyFileToLocal(): {}", copied);
        return file;
    }

    public boolean moveFile(FTPSClient client, String fromPath, String toPath) throws IOException {
        LOGGER.debug(">> moveFile({},{})", fromPath, toPath);
        boolean success = client.rename(fromPath, toPath);
        LOGGER.debug("<< moveFile(): {}", success);
        return success;
    }

    public boolean appendToFile(FTPSClient client, String remotePath, String fileName, String content) throws IOException {
        LOGGER.debug(">> appendToFile({},{},{})", remotePath, fileName, content);
        String filePath = String.format("%s/%s", remotePath, fileName);
        InputStream is = new ByteArrayInputStream(content.getBytes());
        boolean success = client.appendFile(filePath, is);
        LOGGER.debug("<< appendToFile(): {}", success);
        return success;
    }

    RemoteFile extractFileMetadata(FTPFile file) {
        RemoteFile rf = new RemoteFile();
        rf.setName(file.getName());
        rf.setSize(file.getSize());
        rf.setTimestamp(file.getTimestampInstant());
        rf.setType(RemoteFileType.byCode(file.getType()));
        return rf;
    }

}
