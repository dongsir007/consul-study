package com.dosth.common.rpc.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dosth.common.remote.ResponseTip;

public interface RpcService {
	/**
	 * @description 测试feign请求
	 * @param name 名称
	 * @return
	 */
	@RequestMapping("/testFeign")
	public ResponseTip testFeign(@RequestParam("name") String name);

	/**
	 * @description 测试RestTemplate请求
	 * @param name 名称
	 * @return
	 */
	@RequestMapping("/testRestTemplate")
	public ResponseTip testRestTemplate(@RequestParam("name") String name);
}