package com.qdx.mule.connectors.ftps.internal;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class LoggingOutputStream extends OutputStream {

    public static void redirectSysOutAndSysErr(Logger logger) {
        System.setOut(new PrintStream(new LoggingOutputStream(logger, Level.INFO)));
        System.setErr(new PrintStream(new LoggingOutputStream(logger, Level.ERROR)));
    }

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
    private final Logger logger;
    private final Level level;

    public LoggingOutputStream(Logger logger, Level level) {
        this.logger = logger;
        this.level = level;
    }

    @Override
    public void write(int b) {
        if (b == '\n') {
            String line = baos.toString();
            baos.reset();

            if (level == Level.TRACE) {
                logger.trace(line);
            } else if (level == Level.DEBUG) {
                logger.debug(line);
            } else if (level == Level.ERROR) {
                logger.error(line);
            } else if (level == Level.INFO) {
                logger.info(line);
            } else if (level == Level.WARN) {
                logger.warn(line);
            }

        } else {
            baos.write(b);
        }
    }

}
