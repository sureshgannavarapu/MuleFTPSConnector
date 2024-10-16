package com.qdx.mule.connectors.ftps.internal.commands.md;

import com.qdx.mule.connectors.ftps.internal.QDFTPSConnection;
import com.qdx.mule.connectors.ftps.internal.commands.AbstractFtpsClientCommand;
import com.qdx.mule.connectors.ftps.internal.commands.FtpsFileArgs;
import java.io.IOException;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FtpsCreateDirectoryCommand extends AbstractFtpsClientCommand<FtpsFileArgs, Boolean> {

    private static final Logger LOGGER = LogManager.getLogger(FtpsCreateDirectoryCommand.class);

    public FtpsCreateDirectoryCommand(QDFTPSConnection connection) {
        super(connection);
    }

    @Override
    protected Boolean executeCommand(FTPSClient client, FtpsFileArgs args) throws IOException {
        LOGGER.trace("createDirector()");
        return service.createDirectory(client, args.getRemotePath(), args.getFileName());
    }

}
