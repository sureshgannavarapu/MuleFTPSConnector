package com.qdx.mule.connectors.ftps.internal.service;

import org.apache.commons.net.ftp.FTPSClient;

public class ConnectivityResult {

    private boolean connected;
    private String message;
    private FTPSClient client;

    /**
     * Use when successfully connected
     */
    public ConnectivityResult() {
        this.connected = true;
        this.message = "Connected successfully";
    }

    public FTPSClient getClient() {
        return client;
    }

    public void setClient(FTPSClient client) {
        this.client = client;
    }

    /**
     * Use when connection unsuccessful
     *
     * @param failureReason
     */
    public ConnectivityResult(String failureReason) {
        this.connected = false;
        this.message = failureReason;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
