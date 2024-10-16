package com.qdx.mule.connectors.ftps.internal;

import com.qdx.mule.connectors.ftps.internal.errors.FtpsException;
import com.qdx.mule.connectors.ftps.internal.service.ConnectivityResult;
import java.util.TimeZone;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.connection.PoolingConnectionProvider;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

/**
 * This class (as it's name implies) provides connection instances and the
 * funcionality to disconnect and validate those connections.
 * <p>
 * All connection related parameters (values required in order to create a
 * connection) must be declared in the connection providers.
 * <p>
 * This particular example is a {@link PoolingConnectionProvider} which declares
 * that connections resolved by this provider will be pooled and reused. There
 * are other implementations like {@link CachedConnectionProvider} which lazily
 * creates and caches connections or simply {@link ConnectionProvider} if you
 * want a new connection each time something requires one.
 */
public class QDFTPSConnectionProvider implements PoolingConnectionProvider<QDFTPSConnection> {

	private static final Logger LOGGER = LogManager.getLogger(QDFTPSConnectionProvider.class);

	/**
	 * FTP Server Host
	 */
	@DisplayName("FTP Server Host")
	@Parameter
	private String host;

	/**
	 * FTP Server Port
	 */
	@DisplayName("FTP Server Port")
	@Parameter
	private Integer port;

	/**
	 * FTP Account user name
	 */
	@DisplayName("FTP Account Username")
	@Parameter
	private String userName;

	/**
	 * FTP Account password
	 */
	@DisplayName("FTP Account Password")
	@Parameter
	private String password;

	/**
	 * Enable debug mode. Actual FTP logs will be displayed. Password information
	 * will NOT be displayed.
	 */
	@DisplayName("Enable Debug Mode")
	@Parameter
	@Optional(defaultValue = "false")
	private boolean debugMode;

	@DisplayName("FTP Server Timezone")
	@Parameter
	@Optional
	private String timeZoneID = TimeZone.getDefault().getID();

	public QDFTPSConnectionProvider() {
		LOGGER.trace("** QdftpsConnectionProvider()");
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}

	public String getTimeZoneID() {
		return timeZoneID;
	}

	public void setTimeZoneID(String timeZoneID) {
		this.timeZoneID = timeZoneID;
	}

	@Override
	public QDFTPSConnection connect() throws ConnectionException {
		LOGGER.trace(">> connect()");
		QDFTPSConnectionParameters parameters = new QDFTPSConnectionParameters();
		parameters.setEnableDebugMode(debugMode);
		parameters.setHost(host);
		parameters.setPassword(password);
		parameters.setPort(port);
		parameters.setTimeZoneID(timeZoneID);
		parameters.setUserName(userName);

		return new QDFTPSConnection(parameters);
	}

	@Override
	public void disconnect(QDFTPSConnection connection) {
		LOGGER.trace(">> disconnect()");
		try {
			connection.invalidate();
		} catch (Exception e) {
			LOGGER.error("Error while disconnecting [" + connection.getId() + "]: " + e.getMessage(), e);
		}
	}

	@Override
	public ConnectionValidationResult validate(QDFTPSConnection connection) {
		LOGGER.trace(">> validate()");
		try {
			ConnectivityResult status = connection.connect();
			if (status.isConnected()) {
				LOGGER.debug("Connection validation successful");
				boolean loggedIn = connection.login();
				if (loggedIn) {

					return ConnectionValidationResult.success();
				} else {
					return ConnectionValidationResult.failure("Login failed", new FtpsException("Login failure"));
				}
			} else {
				LOGGER.debug("Connection validation failed");
				return ConnectionValidationResult.failure("Connection failure", new FtpsException(status.getMessage()));
			}
		} catch (Exception ex) {
			LOGGER.error("Connection validation failed", ex);
			return ConnectionValidationResult.failure("Connection failure", ex);
		}
	}
}
