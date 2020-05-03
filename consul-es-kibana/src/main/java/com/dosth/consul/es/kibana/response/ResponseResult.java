package com.dosth.consul.es.kibana.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

import com.dosth.consul.es.kibana.exception.ApiException;
import com.dosth.consul.es.kibana.response.enums.CodeEnum;

@Data
@JsonInclude(ALWAYS)
public class ResponseResult<T> {
	
	private String code;
	private String message;
	private T data;
	
	public ResponseResult() {
	}

	public ResponseResult(ApiException e) {
		this.code = e.getCode();
		this.message = e.getMessage();
	}
	
	public ResponseResult(T data) {
		this.code = CodeEnum.SUCCESS.getCode();
		this.message = CodeEnum.SUCCESS.getMessage();
		this.data = data;
	}
	
	public ResponseResult(String code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}
	
	public static <T> ResponseResult<T> success(T data) {
		return new ResponseResult<>(data);
	}

	public static <T> ResponseResult<T> badRequestError(String message) {
		return new ResponseResult<>(CodeEnum.BAD_REQUEST_CODE.getCode(), message, null);
	}

	/**
	 * 返回异常
	 * @param e exception
	 * @return ResponseResult
	 */
	public static <T> ResponseResult<T> fail(Throwable e) {
		if (e instanceof ApiException) {
			return new ResponseResult<>((ApiException) e);
		}
		return new ResponseResult<>(CodeEnum.SYSTEM_ERROR_CODE.getCode(), e.getMessage(), null);
	}

	@SuppressWarnings("rawtypes")
	public static ResponseResult fail(ApiException e, Object data) {
		return new ResponseResult<>(e.getCode(), e.getMessage(), data);
	}
}
