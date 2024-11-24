package com.appsdeveloperblog.ws.emailnotification.error;

public class RetryableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RetryableException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public RetryableException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
}
