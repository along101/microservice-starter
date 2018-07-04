package com.along101.microservice.util.exception;

@SuppressWarnings("serial")
public class FrameworkException extends RuntimeException {
	public String errorCode;
	public String errorCause;
	private String message;

	public FrameworkException(String errorCode) {
		this.errorCode = errorCode;
	}

	public FrameworkException(String errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}

	public FrameworkException(String message, String errorCode) {
		this.message = message;
		this.errorCode = errorCode;
	}

	public FrameworkException(String message, String errorCode, Throwable cause) {
		super(message, cause);
		this.message = message;
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
