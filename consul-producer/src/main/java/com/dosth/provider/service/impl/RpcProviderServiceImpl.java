package com.dosth.provider.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

import com.dosth.common.remote.ResponseTip;
import com.dosth.common.rpc.service.RpcService;

@RestController
@RefreshScope
public class RpcProviderServiceImpl implements RpcService {

	@Value("${server.port}")
	private int port;
	
	@Value("${say}")
	private String say;
	
	@Override
	public ResponseTip testFeign(String name) {
		return new ResponseTip(this.port + " say[Feign]:" + this.say + "," + name + "!");
	}

	@Override
	public ResponseTip testRestTemplate(String name) {
		return new ResponseTip(this.port + " say[RestTemplate]:" + this.say + "," + name + "!");
	}
}