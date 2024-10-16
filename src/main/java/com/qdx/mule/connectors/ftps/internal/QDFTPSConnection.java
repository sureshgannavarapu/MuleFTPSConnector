package com.qdx.mule.connectors.ftps.internal;

import com.qdx.mule.connectors.ftps.internal.errors.FtpsException;
import com.qdx.mule.connectors.ftps.internal.service.CommandStatus;
import com.qdx.mule.connectors.ftps.internal.service.ConnectivityResult;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class represents an extension connection just as example (there is no real connection with
 * anything here c:).
 */
public final class QDFTPSConnection {

    private static final Logger LOGGER = LogManager.getLogger(QDFTPSConnection.class);

    /**
     * Unique ID
     */
    private final String id;

    /**
     * FTP Server Host
     */
    private final String host;

    /**
     * FTP Server Port
     */
    private final Integer port;

    /**
     * FTP Account Username
     */
    private final String userName;

    /**
     * FTP Account Password
     */
    private final String password;

    /**
     * Enable Debug Mode. Passwords will never be displayed in logs.
     */
    private final boolean enabledDebugMode;

    /**
     * FTP Server Timezone ID
     */
    private final String timeZoneID;

    /**
     * FTP Server Language Code
     */
    private final String language;

    /**
     * Low level FTPSClient
     */
    private FTPSClient cachedClient;

    static {
        LoggingOutputStream.redirectSysOutAndSysErr(LOGGER);
    }

    public QDFTPSConnection(QDFTPSConnectionParameters connectionParameters) {
        LOGGER.trace("** QdftpsConnection()");
        this.host = connectionParameters.getHost();
        this.port = connectionParameters.getPort();
        this.userName = connectionParameters.getUserName();
        this.password = connectionParameters.getPassword();
        this.enabledDebugMode = connectionParameters.isEnableDebugMode();
        this.timeZoneID = connectionParameters.getTimeZoneID();
        this.language = connectionParameters.getLanguage();
        this.id = String.format("%s:%d:%s", this.host, this.port, this.userName);
    }

    public String getId() {
        return id;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public boolean isEnabledDebugMode() {
        return enabledDebugMode;
    }

    public String getTimeZoneID() {
        return timeZoneID;
    }

    public String getLanguage() {
        return language;
    }

    public boolean isAlive() {
        return this.cachedClient != null && this.cachedClient.isAvailable() && this.cachedClient.isConnected();
    }

    public Optional<FTPSClient> getClient() {
        return Optional.ofNullable(this.cachedClient);
    }

    public void invalidate() {
        LOGGER.trace(">> invalidate()");
        try {
            if (cachedClient != null && cachedClient.isConnected()) {
                LOGGER.trace("Trying to logout and disconnect");
                //cachedClient.logout();
                cachedClient.disconnect();
                LOGGER.trace("Done");
            }

            LOGGER.trace("<< invalidate()");
        } catch (IOException ex) {
            LOGGER.error("Error when disconnecting from FTP Server", ex);
            throw new FtpsException("Error when disconnecting from FTP Server", ex);
        }
    }

    public ConnectivityResult connect() {
        LOGGER.debug(">> connect({},{},{})", host, port, userName);
        ConnectivityResult cStatus;
        FTPSClient localClient = getInstance();

        try {

            if (!localClient.isConnected()) {
                // Configure server timezone
//                FTPClientConfig config = new FTPClientConfig();
//                config.setServerLanguageCode(this.language);
//                config.setServerTimeZoneId(timeZoneID);
//                localClient.configure(config);

                int reply;

                localClient.connect(host, port);
                LOGGER.debug("Connected to {}:{}", host, port);
                reply = localClient.getReplyCode();

                if (!FTPReply.isPositiveCompletion(reply)) {
                    // ensure we are disconnected
                    localClient.disconnect();
                }
            }

        } catch (IOException ex) {
            throw new FtpsException("Error when connecting to FTPS server", ex);
        } finally {

            if (localClient.isConnected()) {
                LOGGER.debug("Connection successful");
                cStatus = new ConnectivityResult();
                cStatus.setClient(localClient);
            } else {
                int replyCode = localClient.getReplyCode();
                LOGGER.debug("Connection failed: {}", CommandStatus.byReplyCode(replyCode));
                cStatus = new ConnectivityResult(CommandStatus.byReplyCode(replyCode).name());
            }
        }

        return cStatus;
    }

    public boolean login() {
        FTPSClient localClient = getInstance();
        try {
            localClient.setFileType(FTP.BINARY_FILE_TYPE);
            localClient.execPBSZ(0);  // Set protection buffer size
            localClient.execPROT("P"); // Set data channel protection to private
            localClient.enterLocalPassiveMode();
            boolean loggedIn = localClient.login(userName, password);
            if (!loggedIn) {
                int replyCode = localClient.getReplyCode();
                LOGGER.debug("Auth failed: {}", CommandStatus.byReplyCode(replyCode));
                throw new FtpsException("Authentication failed. Reason: " + CommandStatus.byReplyCode(replyCode));
            }

            return loggedIn;
        } catch (IOException ex) {
            LOGGER.error("", ex);
            throw new FtpsException("Error when trying to login", ex);
        }
    }

    FTPSClient getInstance() {
        LOGGER.trace(">> getInstance()");
        if (cachedClient != null && cachedClient.isAvailable() && cachedClient.isConnected()) {
            LOGGER.trace("<< Returning cached client");
            return cachedClient;
        } else {
            cachedClient = new FTPSClient();
            if (enabledDebugMode) {
                cachedClient.addProtocolCommandListener(new PrintCommandListener(System.out, true));
            }

            LOGGER.trace("<< Returnting new instance of client");
            return cachedClient;
        }
    }
}
