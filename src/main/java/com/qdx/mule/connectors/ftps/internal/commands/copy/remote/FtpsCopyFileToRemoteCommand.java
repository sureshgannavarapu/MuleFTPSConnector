package com.qdx.mule.connectors.ftps.internal.commands.copy.remote;

import com.qdx.mule.connectors.ftps.internal.QDFTPSConnection;
import com.qdx.mule.connectors.ftps.internal.commands.AbstractFtpsClientCommand;
import com.qdx.mule.connectors.ftps.internal.commands.FtpsCopyFileArgs;
import java.io.IOException;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FtpsCopyFileToRemoteCommand extends AbstractFtpsClientCommand<FtpsCopyFileArgs, Boolean> {

    private static final Logger LOGGER = LogManager.getLogger(FtpsCopyFileToRemoteCommand.class);

    public FtpsCopyFileToRemoteCommand(QDFTPSConnection configuration) {
        super(configuration);
    }

    @Override
    protected Boolean executeCommand(FTPSClient client, FtpsCopyFileArgs args) throws IOException {
        LOGGER.trace("copyFileToRemote()");
        return service.copyFileToRemote(client, args.getRemotePath(), args.getFileName(), args.getFis());
    }

}
