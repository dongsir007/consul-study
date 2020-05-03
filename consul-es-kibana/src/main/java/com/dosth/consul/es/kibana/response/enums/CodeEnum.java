package com.dosth.consul.es.kibana.response.enums;

import lombok.Getter;

/**
 * 
 * @description CODE枚举
 *
 * @author guozhidong
 */
@Getter
public enum CodeEnum {

	SUCCESS("200", "成功"), BAD_REQUEST_CODE("400", "请求错误"), SYSTEM_ERROR_CODE("500", "操作失败");

	private String code;
	private String message;

	private CodeEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}
}