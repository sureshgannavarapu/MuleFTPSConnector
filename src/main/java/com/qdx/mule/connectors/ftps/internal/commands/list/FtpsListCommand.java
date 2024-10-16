package com.qdx.mule.connectors.ftps.internal.commands.list;

import com.qdx.mule.connectors.ftps.internal.QDFTPSConnection;
import com.qdx.mule.connectors.ftps.internal.commands.AbstractFtpsClientCommand;
import com.qdx.mule.connectors.ftps.internal.commands.FtpsCommandArgs;
import com.qdx.mule.connectors.ftps.internal.domain.RemoteFile;
import java.io.IOException;
import java.util.List;
import org.apache.commons.net.ftp.FTPSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FtpsListCommand extends AbstractFtpsClientCommand<FtpsCommandArgs, List<RemoteFile>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FtpsListCommand.class);

    public FtpsListCommand(QDFTPSConnection connection) {
        super(connection);
    }

    @Override
    protected List<RemoteFile> executeCommand(FTPSClient client, FtpsCommandArgs args) throws IOException {
        LOGGER.trace(">> list()");
        List<RemoteFile> files;
        FileFilter filter = args.getFilter();
        switch (filter) {
            case DIRECTORY:
                files = service.listDirectories(client, args.getRemotePath());
                break;
            case FILE:
                files = service.listFiles(client, args.getRemotePath());
                break;
            default:
                files = service.list(client, args.getRemotePath());
                break;
        }

        return files;
    }

}
