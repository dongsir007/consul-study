package com.dosth.sleuth.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import zipkin.server.EnableZipkinServer;

/**
 * 
 * @description 链路追踪
 *
 * @author guozhidong
 */
@SuppressWarnings("deprecation")
@SpringBootApplication
@EnableZipkinServer
public class SleuthZipkinApplication {
	public static void main(String[] args) {
		SpringApplication.run(SleuthZipkinApplication.class, args);
	}
}
