package com.qdx.mule.connectors.ftps.internal.commands;

public class FtpsMoveFileArgs extends FtpsCommandArgs {

    private String remoteDestinationPath;
    private String fileName;

    public FtpsMoveFileArgs(String remoteSourcePath, String remoteDestinationPath, String file) {
        super(remoteSourcePath);
        this.fileName = file;
        this.remoteDestinationPath = remoteDestinationPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String file) {
        this.fileName = file;
    }

    public String getRemoteDestinationPath() {
        return remoteDestinationPath;
    }

    public void setRemoteDestinationPath(String remoteDestinationPath) {
        this.remoteDestinationPath = remoteDestinationPath;
    }

}
