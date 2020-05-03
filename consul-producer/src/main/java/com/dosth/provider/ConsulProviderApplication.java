package com.dosth.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @description consul服务端
 *
 * @author guozhidong
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ConsulProviderApplication {
	public static void main(String[] args) {
		SpringApplication.run(ConsulProviderApplication.class, args);
	}
}
