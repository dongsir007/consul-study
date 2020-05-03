package com.dosth.consumer.rpc.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.dosth.common.remote.ResponseTip;
import com.dosth.consumer.rpc.RpcConsumeService;

@Component
public class RpcConsumeServiceImpl implements RpcConsumeService {
	
	private static final Logger logger = LoggerFactory.getLogger(RpcConsumeServiceImpl.class);

	@Override
	public ResponseTip testFeign(String name) {
		logger.error("Not found by feign:" + name);
		return new ResponseTip(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
	}

	@Override
	public ResponseTip testRestTemplate(String name) {
		logger.error("Not found by restTemplate:" + name);
		return new ResponseTip(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
	}
}