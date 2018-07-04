package com.along101.microservice.util.exception;

/**
 * 不需要打印堆栈信息，提升性能
 */

@SuppressWarnings("serial")
public class NoStrackException extends FrameworkException {
	public NoStrackException(String message, String errorCode, String className, String method) {
		super(message, errorCode);
		setStackTrace(new StackTraceElement[] { new StackTraceElement(className, method, null, -1) });
	}
}
