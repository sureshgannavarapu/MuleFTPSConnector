package com.qdx.mule.connectors.ftps.internal.commands.copy.local;

import com.qdx.mule.connectors.ftps.internal.QDFTPSConnection;
import com.qdx.mule.connectors.ftps.internal.commands.AbstractFtpsClientCommand;
import com.qdx.mule.connectors.ftps.internal.commands.FtpsFileArgs;
import java.io.File;
import java.io.IOException;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FtpsCopyFileToLocalCommand extends AbstractFtpsClientCommand<FtpsFileArgs, File> {

    private static final Logger LOGGER = LogManager.getLogger(FtpsCopyFileToLocalCommand.class);

    public FtpsCopyFileToLocalCommand(QDFTPSConnection connection) {
        super(connection);
    }

    @Override
    protected File executeCommand(FTPSClient client, FtpsFileArgs args) throws IOException {
        LOGGER.trace("copyFileToLocal()");
        return service.copyFileToLocal(client, args.getRemotePath(), args.getFileName());
    }

}
