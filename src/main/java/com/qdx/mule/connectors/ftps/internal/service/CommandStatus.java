package com.qdx.mule.connectors.ftps.internal.service;

import com.qdx.mule.connectors.ftps.internal.errors.FtpsException;
import org.apache.commons.net.ftp.FTPReply;

public enum CommandStatus {
    ACTION_ABORTED(FTPReply.ACTION_ABORTED),
    BAD_COMMAND_SEQUENCE(FTPReply.BAD_COMMAND_SEQUENCE),
    BAD_TLS_NEGOTIATION_OR_DATA_ENCRYPTION_REQUIRED(FTPReply.BAD_TLS_NEGOTIATION_OR_DATA_ENCRYPTION_REQUIRED),
    CANNOT_OPEN_DATA_CONNECTION(FTPReply.CANNOT_OPEN_DATA_CONNECTION),
    CLOSING_DATA_CONNECTION(FTPReply.CLOSING_DATA_CONNECTION),
    COMMAND_IS_SUPERFLUOUS(FTPReply.COMMAND_IS_SUPERFLUOUS),
    COMMAND_NOT_IMPLEMENTED(FTPReply.COMMAND_NOT_IMPLEMENTED),
    COMMAND_NOT_IMPLEMENTED_FOR_PARAMETER(FTPReply.COMMAND_NOT_IMPLEMENTED_FOR_PARAMETER),
    COMMAND_OK(FTPReply.COMMAND_OK),
    DATA_CONNECTION_ALREADY_OPEN(FTPReply.DATA_CONNECTION_ALREADY_OPEN),
    DATA_CONNECTION_OPEN(FTPReply.DATA_CONNECTION_OPEN),
    DENIED_FOR_POLICY_REASONS(FTPReply.DENIED_FOR_POLICY_REASONS),
    DIRECTORY_STATUS(FTPReply.DIRECTORY_STATUS),
    ENTERING_EPSV_MODE(FTPReply.ENTERING_EPSV_MODE),
    ENTERING_PASSIVE_MODE(FTPReply.ENTERING_PASSIVE_MODE),
    EXTENDED_PORT_FAILURE(FTPReply.EXTENDED_PORT_FAILURE),
    FAILED_SECURITY_CHECK(FTPReply.FAILED_SECURITY_CHECK),
    FILE_ACTION_NOT_TAKEN(FTPReply.FILE_ACTION_NOT_TAKEN),
    FILE_ACTION_OK(FTPReply.FILE_ACTION_OK),
    FILE_ACTION_PENDING(FTPReply.FILE_ACTION_PENDING),
    FILE_NAME_NOT_ALLOWED(FTPReply.FILE_NAME_NOT_ALLOWED),
    FILE_STATUS(FTPReply.FILE_STATUS),
    FILE_STATUS_OK(FTPReply.FILE_STATUS_OK),
    FILE_UNAVAILABLE(FTPReply.FILE_UNAVAILABLE),
    HELP_MESSAGE(FTPReply.HELP_MESSAGE),
    INSUFFICIENT_STORAGE(FTPReply.INSUFFICIENT_STORAGE),
    NAME_SYSTEM_TYPE(FTPReply.NAME_SYSTEM_TYPE),
    NEED_ACCOUNT(FTPReply.NEED_ACCOUNT),
    NEED_ACCOUNT_FOR_STORING_FILES(FTPReply.NEED_ACCOUNT_FOR_STORING_FILES),
    NEED_PASSWORD(FTPReply.NEED_PASSWORD),
    NOT_LOGGED_IN(FTPReply.NOT_LOGGED_IN),
    PAGE_TYPE_UNKNOWN(FTPReply.PAGE_TYPE_UNKNOWN),
    PATHNAME_CREATED(FTPReply.PATHNAME_CREATED),
    REQUEST_DENIED(FTPReply.REQUEST_DENIED),
    REQUESTED_PROT_LEVEL_NOT_SUPPORTED(FTPReply.REQUESTED_PROT_LEVEL_NOT_SUPPORTED),
    RESTART_MARKER(FTPReply.RESTART_MARKER),
    SECURITY_DATA_EXCHANGE_COMPLETE(FTPReply.SECURITY_DATA_EXCHANGE_COMPLETE),
    SECURITY_DATA_EXCHANGE_SUCCESSFULLY(FTPReply.SECURITY_DATA_EXCHANGE_SUCCESSFULLY),
    SECURITY_DATA_IS_ACCEPTABLE(FTPReply.SECURITY_DATA_IS_ACCEPTABLE),
    SECURITY_MECHANISM_IS_OK(FTPReply.SECURITY_MECHANISM_IS_OK),
    SERVICE_CLOSING_CONTROL_CONNECTION(FTPReply.SERVICE_CLOSING_CONTROL_CONNECTION),
    SERVICE_NOT_AVAILABLE(FTPReply.SERVICE_NOT_AVAILABLE),
    SERVICE_NOT_READY(FTPReply.SERVICE_NOT_READY),
    SERVICE_READY(FTPReply.SERVICE_READY),
    STORAGE_ALLOCATION_EXCEEDED(FTPReply.STORAGE_ALLOCATION_EXCEEDED),
    SYNTAX_ERROR_IN_ARGUMENTS(FTPReply.SYNTAX_ERROR_IN_ARGUMENTS),
    SYSTEM_STATUS(FTPReply.SYSTEM_STATUS),
    TRANSFER_ABORTED(FTPReply.TRANSFER_ABORTED),
    UNAVAILABLE_RESOURCE(FTPReply.UNAVAILABLE_RESOURCE),
    UNRECOGNIZED_COMMAND(FTPReply.UNRECOGNIZED_COMMAND),
    USER_LOGGED_IN(FTPReply.USER_LOGGED_IN);

    private final int replyCode;

    private CommandStatus(int replyCode) {
        this.replyCode = replyCode;
    }

    public int getReplyCode() {
        return replyCode;
    }

    public static CommandStatus byReplyCode(int code) {
        for (CommandStatus status : values()) {
            if (status.getReplyCode() == code) {
                return status;
            }
        }

        throw new FtpsException(String.format("Unknown command status: %d", code));
    }
}
