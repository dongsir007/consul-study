package com.dosth.consul.es.kibana;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ConsulEsKibanaApp {
	public static void main(String[] args) {
		SpringApplication.run(ConsulEsKibanaApp.class, args);
	}
}
