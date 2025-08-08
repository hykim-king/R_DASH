package com.pcwk.ehr.service;

public class BotServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BotServiceException(String message) {
		super(message);
	}

	public BotServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}