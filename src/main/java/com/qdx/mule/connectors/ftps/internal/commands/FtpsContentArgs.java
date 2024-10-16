package com.qdx.mule.connectors.ftps.internal.commands;

public class FtpsContentArgs extends FtpsFileArgs {

    private String content;

    public FtpsContentArgs(String remotePath, String file, String content) {
        super(remotePath, file);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}
