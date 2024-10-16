package com.qdx.mule.connectors.ftps.internal.errors;

public class FtpsException extends RuntimeException {

    public FtpsException() {
    }

    public FtpsException(String message) {
        super(message);
    }

    public FtpsException(String message, Throwable cause) {
        super(message, cause);
    }

    public FtpsException(Throwable cause) {
        super(cause);
    }

    public FtpsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
