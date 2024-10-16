package com.qdx.mule.connectors.ftps.internal.commands;

import java.io.InputStream;

public class FtpsCopyFileArgs extends FtpsCommandArgs {

    private String fileName;
    private InputStream fis;

    public FtpsCopyFileArgs(String remotePath, String fileName, InputStream file) {
        super(remotePath);
        this.fileName = fileName;
        this.fis = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public InputStream getFis() {
        return fis;
    }

    public void setFis(InputStream fis) {
        this.fis = fis;
    }

}
