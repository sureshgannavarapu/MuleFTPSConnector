package com.qdx.mule.connectors.ftps.internal;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

/**
 * This class represents an extension configuration, values set in this class are commonly used
 * across multiple operations since they represent something core from the extension.
 */
@Operations(QDFTPSOperations.class)
@ConnectionProviders(QDFTPSConnectionProvider.class)
public class QDFTPSConfiguration {

    @DisplayName("Base Directory")
    @Parameter
    private String baseDirectory;
//
//    @DisplayName("Log Level")
//    @Parameter
//    @Optional(defaultValue = "INFO")
//    private Level logLevel = Level.INFO;

    public QDFTPSConfiguration() {
    }

    public String getBaseDirectory() {
        return baseDirectory;
    }

    public void setBaseDirectory(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

	/*
	 * public Level getLogLevel() { return logLevel; }
	 */

//    public void setLogLevel(Level logLevel) {
//        this.logLevel = logLevel;
//        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
//        org.apache.logging.log4j.core.config.Configuration config = ctx.getConfiguration();
//        LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
//        loggerConfig.setLevel(logLevel);
//        ctx.updateLoggers();
//    }

}
