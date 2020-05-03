package com.dosth.hystrix.tuibine.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * 
 * @description Hystrix turbine dashboard
 *
 * @author guozhidong
 */
@SpringBootApplication
@EnableTurbine
@EnableHystrix
@EnableHystrixDashboard
@EnableDiscoveryClient
public class HystrixTurbineDashboardApplication {
	public static void main(String[] args) {
		SpringApplication.run(HystrixTurbineDashboardApplication.class, args);
	}
}
