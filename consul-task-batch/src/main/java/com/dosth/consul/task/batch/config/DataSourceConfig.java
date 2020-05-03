package com.dosth.consul.task.batch.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.task.configuration.DefaultTaskConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public DefaultTaskConfigurer getTaskConfigurer() {
		return new DefaultTaskConfigurer(dataSource);
	}
}