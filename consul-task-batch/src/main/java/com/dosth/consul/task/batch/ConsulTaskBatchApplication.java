package com.dosth.consul.task.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;

@EnableTask
@SpringBootApplication
public class ConsulTaskBatchApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(ConsulTaskBatchApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(ConsulTaskBatchApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner commandLineRunner() {
		return new HelloWordCommandLineRunner();
	}
	
	class HelloWordCommandLineRunner implements CommandLineRunner {
		@Override
		public void run(String... args) throws Exception {
			logger.info("running a task!!!");
		}
	}
}
