package com.qdx.mule.connectors.ftps.internal;

import com.qdx.mule.connectors.ftps.internal.commands.FtpsCommandArgs;
import com.qdx.mule.connectors.ftps.internal.commands.FtpsContentArgs;
import com.qdx.mule.connectors.ftps.internal.commands.FtpsCopyFileArgs;
import com.qdx.mule.connectors.ftps.internal.commands.FtpsFileArgs;
import com.qdx.mule.connectors.ftps.internal.commands.FtpsMoveFileArgs;
import com.qdx.mule.connectors.ftps.internal.commands.copy.local.FtpsCopyFileToLocalCommand;
import com.qdx.mule.connectors.ftps.internal.commands.copy.remote.FtpsCopyFileToRemoteCommand;
import com.qdx.mule.connectors.ftps.internal.commands.delete.FtpsDeleteFileCommand;
import com.qdx.mule.connectors.ftps.internal.commands.list.FileFilter;
import com.qdx.mule.connectors.ftps.internal.commands.list.FtpsListCommand;
import com.qdx.mule.connectors.ftps.internal.commands.md.FtpsCreateDirectoryCommand;
import com.qdx.mule.connectors.ftps.internal.commands.move.FtpsMoveFileCommand;
import com.qdx.mule.connectors.ftps.internal.commands.write.AppendCommand;
import com.qdx.mule.connectors.ftps.internal.commands.write.WriteMode;
import com.qdx.mule.connectors.ftps.internal.domain.RemoteFile;
import com.qdx.mule.connectors.ftps.internal.utils.StringUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Connection;
import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

/**
 * This class is a container for operations, every public method in this class will be taken as an
 * extension operation.
 */
public class QDFTPSOperations {

    private static final Logger LOGGER = LogManager.getLogger(QDFTPSOperations.class);

    public QDFTPSOperations() {
    }

    /**
     * Copies a file to the specified path on the FTP Server
     *
     * @param connection
     * @param config
     * @param directoryPath
     * @param fileName
     * @param payload
     * @return true if file is copied successfully; false otherwise.
     */
    @Summary("Copies a file to the specified path on the FTP Server")
    @MediaType(value = ANY, strict = false)
    public boolean copyFileToRemote(
            @Connection QDFTPSConnection connection,
            @Config QDFTPSConfiguration config,
            @Summary("Complete path to directory") @org.mule.runtime.extension.api.annotation.param.Optional String directoryPath,
            String fileName,
            String payload) {

        String rPath = config.getBaseDirectory();
        if (directoryPath != null && !StringUtils.isBlank(directoryPath)) {
            rPath = StringUtils.getIsoEncodedString(directoryPath);
        }

        LOGGER.debug(">> copyFileToRemote({}, {})", fileName, rPath);

        boolean copied;
        String baseName = FilenameUtils.getBaseName(fileName);
        String extension = FilenameUtils.getExtension(fileName);
        try {
            File tempFile = File.createTempFile(baseName, extension);
            Files.write(Paths.get(tempFile.getPath()), payload.getBytes(StandardCharsets.UTF_8));
            FileInputStream fis = new FileInputStream(tempFile);

            FtpsCopyFileArgs args = new FtpsCopyFileArgs(
                    rPath,
                    fileName,
                    fis);

            FtpsCopyFileToRemoteCommand command = new FtpsCopyFileToRemoteCommand(connection);
            copied = command.execute(args);
        } catch (Exception ex) {
            LOGGER.error("copyFileToRemote failed", ex);
            copied = false;
        }

        LOGGER.debug("<< copyFileToRemote(): {}", copied);
        return copied;
    }

    /**
     * Downloads the file specified from the FTP Server
     *
     * @param connection
     * @param config
     * @param directoryPath
     * @param fileName
     * @return file specified by the path on the FTP server
     */
    @Summary("Downloads the specified file from the FTP Server")
    @MediaType(value = ANY, strict = false)
    public Optional<File> copyFileToLocal(
            @Connection QDFTPSConnection connection,
            @Config QDFTPSConfiguration config,
            @Summary("Complete path to directory") @org.mule.runtime.extension.api.annotation.param.Optional String directoryPath,
            String fileName) {

        String rPath = config.getBaseDirectory();
        if (directoryPath != null && !StringUtils.isBlank(directoryPath)) {
            rPath = StringUtils.getIsoEncodedString(directoryPath);
        }

        LOGGER.debug(">> copyFileToLocal({}, {})", rPath, fileName);

        try {
            FtpsFileArgs args = new FtpsFileArgs(rPath, fileName);
            FtpsCopyFileToLocalCommand command = new FtpsCopyFileToLocalCommand(connection);
            File file = command.execute(args);

            LOGGER.debug("<< copyFileToLocal(): {}", file);
            return Optional.of(file);
        } catch (Exception ex) {
            LOGGER.error("copyFileToLocal failed", ex);
            return Optional.empty();
        }
    }

    /**
     * Reads the content of the file specified on the FTP server.
     *
     * @param connection
     * @param config
     * @param directoryPath
     * @param fileName
     * @return Content of the specified file
     */
    @Summary("Reads the content of the specified file on the FTP Server")
    @MediaType(value = ANY, strict = false)
    public Optional<String> read(
            @Connection QDFTPSConnection connection,
            @Config QDFTPSConfiguration config,
            @Summary("Complete path to directory") @org.mule.runtime.extension.api.annotation.param.Optional String directoryPath,
            String fileName) {
        String rPath = config.getBaseDirectory();
        if (directoryPath != null && !StringUtils.isBlank(directoryPath)) {
            rPath = StringUtils.getIsoEncodedString(directoryPath);
        }

        LOGGER.debug(">> read({}, {})", rPath, fileName);

        try {
            Optional<File> fileWrapper = copyFileToLocal(connection, config, rPath, fileName);
            if (fileWrapper.isPresent()) {
                File file = fileWrapper.get();
                String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
                return Optional.ofNullable(content);
            }

            LOGGER.debug("<< read(): Error or File not found");
            return Optional.empty();
        } catch (IOException ex) {
            LOGGER.error("copyFileToLocal failed", ex);
            return Optional.empty();
        }
    }

    /**
     * Writes/appends content to the specified file on the FTP Server.
     *
     * @param connection
     * @param config
     * @param directoryPath
     * @param fileName
     * @param mode
     * @param content
     * @return true if the operation is successful
     */
    @Summary("Writes/appends content to the specified file on the FTP Server")
    @MediaType(value = ANY, strict = false)
    public boolean write(
            @Connection QDFTPSConnection connection,
            @Config QDFTPSConfiguration config,
            @Summary("Complete path to directory") @org.mule.runtime.extension.api.annotation.param.Optional String directoryPath,
            String fileName,
            WriteMode mode,
            String content) {
        String rPath = config.getBaseDirectory();
        if (directoryPath != null && !StringUtils.isBlank(directoryPath)) {
            rPath = StringUtils.getIsoEncodedString(directoryPath);
        }

        LOGGER.debug(">> write({}, {}, {})", rPath, fileName, mode);

        boolean success;

        if (mode == WriteMode.OVERWRITE || mode == WriteMode.NEW) {
            success = copyFileToRemote(connection, config, directoryPath, fileName, content);
        } else {
            try {
                FtpsContentArgs args = new FtpsContentArgs(rPath, fileName, content);
                AppendCommand command = new AppendCommand(connection);
                success = command.execute(args);
            } catch (Exception ex) {
                LOGGER.error("write() failed", ex);
                success = false;
            }
        }

        return success;
    }

    /**
     * Lists files and directories in the specified location on the FTP Server
     *
     * @param connection
     * @param config
     * @param directoryPath
     * @param filter
     * @return list of files and/or directories based on the specified filter
     */
    @Summary("Lists files and directories in the specified location on the FTP Server")
    @MediaType(value = ANY, strict = false)
    public List<RemoteFile> list(
            @Connection QDFTPSConnection connection,
            @Config QDFTPSConfiguration config,
            @Summary("Complete path to directory") @org.mule.runtime.extension.api.annotation.param.Optional String directoryPath,
            FileFilter filter) {
        String rPath = config.getBaseDirectory();
        if (directoryPath != null && !StringUtils.isBlank(directoryPath)) {
            rPath = StringUtils.getIsoEncodedString(directoryPath);
        }

        LOGGER.debug(">> list({}, {})", rPath, filter);
        // No exception handling. We want FtpsException to be propogated to the client
        FtpsCommandArgs args = new FtpsCommandArgs(rPath);
        FtpsListCommand command = new FtpsListCommand(connection);
        return command.execute(args);
    }

    /**
     * Move file from source path on the FTP Server to destination path
     *
     * @param connection
     * @param config
     * @param sourceDirectoryPath
     * @param destinationDirectoryPath
     * @param fileName
     * @return true when move is successful
     */
    @Summary("Move file from source path on the FTP Server to destination path")
    @MediaType(value = ANY, strict = false)
    public boolean moveFile(
            @Connection QDFTPSConnection connection,
            @Config QDFTPSConfiguration config,
            @Summary("Complete path to directory") @org.mule.runtime.extension.api.annotation.param.Optional String sourceDirectoryPath,
            String destinationDirectoryPath, String fileName) {

        boolean success;
        String rPath = config.getBaseDirectory();
        if (sourceDirectoryPath != null && !StringUtils.isBlank(sourceDirectoryPath)) {
            rPath = sourceDirectoryPath;
        }

        LOGGER.debug("moveFile({}, {}, {})", fileName, rPath, destinationDirectoryPath);

        try {
            FtpsMoveFileArgs args = new FtpsMoveFileArgs(rPath, destinationDirectoryPath, fileName);
            FtpsMoveFileCommand command = new FtpsMoveFileCommand(connection);
            success = command.execute(args);
        } catch (Exception ex) {
            LOGGER.error("Failed to move file", ex);
            success = false;
        }

        return success;
    }

    /**
     * Deletes the specified file on the FTP Server
     *
     * @param connection
     * @param config
     * @param directoryPath
     * @param fileName
     * @return true if the op is a success
     */
    @Summary("Deletes the specified file on the FTP Server")
    @MediaType(value = ANY, strict = false)
    public boolean deleteFile(
            @Connection QDFTPSConnection connection,
            @Config QDFTPSConfiguration config,
            @Summary("Complete path to directory") @org.mule.runtime.extension.api.annotation.param.Optional String directoryPath,
            String fileName) {
        boolean success;
        String rPath = config.getBaseDirectory();
        if (directoryPath != null && !StringUtils.isBlank(directoryPath)) {
            rPath = StringUtils.getIsoEncodedString(directoryPath);
        }

        LOGGER.debug("deleteFile({}, {})", rPath, fileName);

        try {
            FtpsFileArgs args = new FtpsFileArgs(rPath, fileName);
            FtpsDeleteFileCommand command = new FtpsDeleteFileCommand(connection);
            success = command.execute(args);
        } catch (Exception ex) {
            LOGGER.error("Delete file failed.", ex);
            success = false;
        }

        return success;
    }

    public boolean createDirectory(
            @Connection QDFTPSConnection connection,
            @Config QDFTPSConfiguration config,
            @Summary("Complete path to directory") @org.mule.runtime.extension.api.annotation.param.Optional String directoryPath,
            String directoryName
    ) {
        boolean success;
        String rPath = config.getBaseDirectory();
        if (directoryPath != null && !StringUtils.isBlank(directoryPath)) {
            rPath = StringUtils.getIsoEncodedString(directoryPath);
        }

        LOGGER.debug("createDirectory({}, {})", rPath, directoryName);

        try {
            FtpsFileArgs args = new FtpsFileArgs(rPath, directoryName);
            FtpsCreateDirectoryCommand command = new FtpsCreateDirectoryCommand(connection);
            success = command.execute(args);
        } catch (Exception ex) {
            LOGGER.error("Create directory failed.", ex);
            success = false;
        }

        return success;
    }
}
