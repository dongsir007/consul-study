package com.dosth.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CallHelloController {

	@Autowired
	private LoadBalancerClient loadBalancerClient;
	
	@RequestMapping("/call")
	public String call() {
		ServiceInstance serviceInstance = this.loadBalancerClient.choose("consul-provider");
		System.err.println("HOST:" + serviceInstance.getHost());
		System.err.println("服务地址:" + serviceInstance.getUri());
		System.err.println("服务名称:" + serviceInstance.getServiceId());
		
		String callServiceResult = new RestTemplate().getForObject(serviceInstance.getUri().toString() + "/rpc/testFeign?name=" + "阿牛", String.class);
		System.err.println(callServiceResult);
		return callServiceResult;
	}
}