package com.qdx.mule.connectors.ftps.internal.domain;

import org.apache.commons.net.ftp.FTPFile;

public enum RemoteFileType {

    DIRECTORY(FTPFile.DIRECTORY_TYPE),
    FILE(FTPFile.FILE_TYPE),
    SYMBOLIC_LINK(FTPFile.SYMBOLIC_LINK_TYPE),
    UNKNOWN(FTPFile.UNKNOWN_TYPE);

    private final int code;

    private RemoteFileType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static RemoteFileType byCode(int code) {

        for (RemoteFileType t : values()) {
            if (t.getCode() == code) {
                return t;
            }
        }

        return UNKNOWN;
    }
}
