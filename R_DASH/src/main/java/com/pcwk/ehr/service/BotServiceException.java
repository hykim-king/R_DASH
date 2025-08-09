package com.pcwk.ehr.service;

public class BotServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String errorCode; // 예: "EMPTY_QUESTION"
    private final int status;       // 예: 400, 500 등

    public BotServiceException(String message) {
        super(message);
        this.errorCode = null;
        this.status = 500;
    }

    public BotServiceException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
        this.status = 500;
    }

    public BotServiceException(String message, String errorCode, int status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getStatus() {
        return status;
    }
}