package com.dosth.consul.task.batch.batchprocessing;

import java.text.Format;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {
	
	private static final Logger logger = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		logger.info("before status>>>" + jobExecution.getStatus().name() + ",startTime:" + format.format(jobExecution.getStartTime()));
		super.beforeJob(jobExecution);
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			logger.info("!!! JOB FINISHED! Time to verify the results");
			this.jdbcTemplate.query("SELECT first_name, last_name FROM people", 
				(rs, row) -> new Person(rs.getString(1), rs.getString(2)))
			.forEach(person -> logger.info("Found <" + person + "> in the database"));
		}
	}
}