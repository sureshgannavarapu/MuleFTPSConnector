package com.qdx.mule.connectors.ftps.internal.commands;

import com.qdx.mule.connectors.ftps.internal.commands.list.FileFilter;

public class FtpsCommandArgs {

    private String remotePath;
    private FileFilter filter = FileFilter.ANY;

    public FtpsCommandArgs() {
    }

    public FtpsCommandArgs(String remotePath) {
        this.remotePath = remotePath;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public FileFilter getFilter() {
        return filter;
    }

    public void setFilter(FileFilter filter) {
        if (filter != null) {
            this.filter = filter;
        }

    }

}
