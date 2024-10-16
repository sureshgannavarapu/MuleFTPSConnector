package com.qdx.mule.connectors.ftps.internal;

import java.util.Locale;
import java.util.TimeZone;

public class QDFTPSConnectionParameters {

    private String host;
    private Integer port;
    private String userName;
    private String password;
    private boolean enableDebugMode;
    private String timeZoneID = TimeZone.getDefault().getID();
    private String language = Locale.getDefault().getLanguage();

    public QDFTPSConnectionParameters() {
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

    public boolean isEnableDebugMode() {
        return enableDebugMode;
    }

    public void setEnableDebugMode(boolean enableDebugMode) {
        this.enableDebugMode = enableDebugMode;
    }

    public String getTimeZoneID() {
        return timeZoneID;
    }

    public void setTimeZoneID(String timeZoneID) {
        this.timeZoneID = timeZoneID;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
