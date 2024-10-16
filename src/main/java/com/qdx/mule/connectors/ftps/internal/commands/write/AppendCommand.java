package com.qdx.mule.connectors.ftps.internal.commands.write;

import com.qdx.mule.connectors.ftps.internal.QDFTPSConnection;
import com.qdx.mule.connectors.ftps.internal.commands.AbstractFtpsClientCommand;
import com.qdx.mule.connectors.ftps.internal.commands.FtpsContentArgs;
import java.io.IOException;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppendCommand extends AbstractFtpsClientCommand<FtpsContentArgs, Boolean> {

    private static final Logger LOGGER = LogManager.getLogger(AppendCommand.class);

    public AppendCommand(QDFTPSConnection configuration) {
        super(configuration);
    }

    @Override
    protected Boolean executeCommand(FTPSClient client, FtpsContentArgs args) throws IOException {
        LOGGER.trace("copyFileToLocal()");
        return service.appendToFile(client, args.getRemotePath(), args.getFileName(), args.getContent());
    }

}
