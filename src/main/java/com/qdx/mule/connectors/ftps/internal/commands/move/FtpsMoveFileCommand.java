package com.qdx.mule.connectors.ftps.internal.commands.move;

import com.qdx.mule.connectors.ftps.internal.QDFTPSConnection;
import com.qdx.mule.connectors.ftps.internal.commands.AbstractFtpsClientCommand;
import com.qdx.mule.connectors.ftps.internal.commands.FtpsMoveFileArgs;
import java.io.IOException;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FtpsMoveFileCommand extends AbstractFtpsClientCommand<FtpsMoveFileArgs, Boolean> {

    private static final Logger LOGGER = LogManager.getLogger(FtpsMoveFileCommand.class);

    public FtpsMoveFileCommand(QDFTPSConnection connection) {
        super(connection);
    }

    @Override
    protected Boolean executeCommand(FTPSClient client, FtpsMoveFileArgs args) throws IOException {
        LOGGER.trace("moveFile()");
        String sourceFile = String.format("%s/%s", args.getRemotePath(), args.getFileName());
        String destFile = String.format("%s/%s", args.getRemoteDestinationPath(), args.getFileName());
        return service.moveFile(client, sourceFile, destFile);
    }

}
