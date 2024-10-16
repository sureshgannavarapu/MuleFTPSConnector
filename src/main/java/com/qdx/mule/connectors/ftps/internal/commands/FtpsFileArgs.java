package com.qdx.mule.connectors.ftps.internal.commands;

public class FtpsFileArgs extends FtpsCommandArgs {

    private String fileName;

    public FtpsFileArgs(String remotePath, String file) {
        super(remotePath);
        this.fileName = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String file) {
        this.fileName = file;
    }

}
