package com.along101.microservice.error;

public interface CommonErrorMessage {
	public static final String APPID_NOT_FOUND = "com.along101.appId must be defined in src/main/resources/application.properties";
	public static final String SERVICE_NAME_NOT_FOUND = "spring.application.name must be defined in src/main/resources/application.properties";
}
