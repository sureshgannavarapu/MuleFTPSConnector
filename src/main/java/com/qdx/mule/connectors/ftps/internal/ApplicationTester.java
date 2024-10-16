package com.qdx.mule.connectors.ftps.internal;

import com.qdx.mule.connectors.ftps.internal.commands.list.FileFilter;
import com.qdx.mule.connectors.ftps.internal.commands.write.WriteMode;
import com.qdx.mule.connectors.ftps.internal.domain.RemoteFile;
import java.util.List;
import java.util.Optional;

/**
 *
 *
 */
public class ApplicationTester {

    public static void main(String[] args) {
        QDFTPSConnectionParameters parameters = new QDFTPSConnectionParameters();
        parameters.setHost("sam-ftps.qdx.com");
        parameters.setPort(21);
        parameters.setUserName("questus\\zz_samftp_StarLims");
        parameters.setPassword("Yy44e7zhPY");
        parameters.setEnableDebugMode(true);

        QDFTPSConnection conn = new QDFTPSConnection(parameters);

        QDFTPSConfiguration config = new QDFTPSConfiguration();
        config.setBaseDirectory("/StarLims_QA");

        QDFTPSOperations ops = new QDFTPSOperations();

        // List files fox
        System.out.println("Listing files");
        List<RemoteFile> remoteFiles = ops.list(conn, config, null, FileFilter.ANY);
        System.out.println("Remote file count: " + remoteFiles.size());

        // Upload a file
        System.out.println("Copy to remote");
        boolean copiedtoRemote = ops.copyFileToRemote(conn, config, null, "test-abc.txt", "The quick brown fox jumps over the lazy dog");
        System.out.println("Copy: " + copiedtoRemote);

        System.out.println("List again");
        remoteFiles = ops.list(conn, config, null, FileFilter.ANY);
        System.out.println("List again count: " + remoteFiles.size());

        // Read 
        System.out.println("Read file content");
        Optional<String> content = ops.read(conn, config, null, "test-abc.txt");
        System.out.println(content.orElse("Content does not exist"));

        // Append
        System.out.println("Append to file");
        boolean appended = ops.write(conn, config, null, "test-abc.txt", WriteMode.APPEND, "\r\nNew content");
        System.out.println("Content appended: " + appended);

        System.out.println("Read again");
        Optional<String> contentAgain = ops.read(conn, config, null, "test-abc.txt");
        System.out.println(contentAgain.orElse("Content does not exist"));

        // Move
        System.out.println("Move file");
        boolean moved = ops.moveFile(conn, config, null, "/StarLims_QA/another", "test-abc.txt");
        System.out.println("Moved: " + moved);

        // Delete
        System.out.println("Delete file");
        boolean deleted = ops.deleteFile(conn, config, null, "test-abc.txt");
        System.out.println("Deleted: " + deleted);
    }
}
