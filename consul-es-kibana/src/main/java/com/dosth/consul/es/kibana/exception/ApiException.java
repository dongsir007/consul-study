package com.dosth.consul.es.kibana.exception;

@SuppressWarnings("serial")
public class ApiException extends RuntimeException {

	private String code;
	private String message;

	public ApiException(String code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ApiException [code=" + code + ", message=" + message + "]";
	}
}