package com.qdx.mule.connectors.ftps.internal.commands.delete;

import com.qdx.mule.connectors.ftps.internal.QDFTPSConnection;
import com.qdx.mule.connectors.ftps.internal.commands.AbstractFtpsClientCommand;
import com.qdx.mule.connectors.ftps.internal.commands.FtpsFileArgs;
import java.io.IOException;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FtpsDeleteFileCommand extends AbstractFtpsClientCommand<FtpsFileArgs, Boolean> {

    private static final Logger LOGGER = LogManager.getLogger(FtpsDeleteFileCommand.class);

    public FtpsDeleteFileCommand(QDFTPSConnection connection) {
        super(connection);
    }

    @Override
    protected Boolean executeCommand(FTPSClient client, FtpsFileArgs args) throws IOException {
        LOGGER.trace("deleteFile()");
        return service.deleteFile(client, String.format("%s/%s", args.getRemotePath(), args.getFileName()));
    }

}
