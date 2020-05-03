package com.dosth.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.dosth.common.remote.ResponseTip;
import com.dosth.consumer.rpc.RpcConsumeService;

@RestController
@RefreshScope
public class ConsulConsumeController {
	
	@Autowired
	private RpcConsumeService rpcConsumeService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private LoadBalancerClient loadBalancerClient;
	
	@RequestMapping("/getFeign")
	public ResponseTip getFeign(@RequestParam("name") String name) {
		return this.rpcConsumeService.testFeign(name);
	}
	
	@RequestMapping("/getRestTemplate")
	public ResponseEntity<ResponseTip> getRestTemplate(@RequestParam("name") String name) {
		ServiceInstance serviceInstance = this.loadBalancerClient.choose("consul-provider");
		System.out.println(serviceInstance.getUri());
		System.out.println(serviceInstance.getPort());
		return this.restTemplate.getForEntity(String.format("http://consul-provider/testRestTemplate?name=%s", name), ResponseTip.class);
	}
}