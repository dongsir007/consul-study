package com.dosth.consul.es.kibana.exception.factory;

import com.dosth.consul.es.kibana.exception.ApiException;

public interface ApiExceptionFactory {
	String prefix();
	default ApiException apply(String errorCode, String errorMsg) {
		return new ApiException(this.prefix() + errorCode, errorMsg);
	}
}
