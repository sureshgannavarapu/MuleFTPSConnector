package com.qdx.mule.connectors.ftps.internal.commands;

import com.qdx.mule.connectors.ftps.internal.QDFTPSConnection;
import com.qdx.mule.connectors.ftps.internal.errors.FtpsException;
import com.qdx.mule.connectors.ftps.internal.service.ConnectivityResult;
import com.qdx.mule.connectors.ftps.internal.service.FTPSService;
import java.io.IOException;
import java.time.Duration;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractFtpsClientCommand<I extends FtpsCommandArgs, O> {

    private static final Logger LOGGER = LogManager.getLogger(AbstractFtpsClientCommand.class);

    protected final FTPSService service = new FTPSService();
    protected final QDFTPSConnection connection;

    protected AbstractFtpsClientCommand(QDFTPSConnection configuration) {
        this.connection = configuration;
    }

    public O execute(I input) {
        FTPSClient client = null;
        O output = null;
        ConnectivityResult result = connection.connect();

        if (!result.isConnected()) {
            LOGGER.error("Connection to FTP server not available");
            throw new FtpsException("Connection to FTP Server not available.");
        }

        try {
            client = result.getClient();
            client.setControlKeepAliveTimeout(Duration.ofMinutes(5));

            boolean loggedIn = connection.login();
            if (loggedIn) {
                output = executeCommand(client, input);
            }

        } catch (Exception ex) {
            throw new FtpsException(ex);
        } finally {
            if (client != null) {
                try {
                    client.logout();
                } catch (Exception ignore) {

                }
            }
        }

        return output;
    }

    protected abstract O executeCommand(FTPSClient client, I args) throws IOException;

}
