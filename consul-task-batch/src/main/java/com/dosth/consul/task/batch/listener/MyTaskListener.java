package com.dosth.consul.task.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.task.listener.TaskExecutionListener;
import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.stereotype.Component;

/**
 * 
 * @description 监听任务状态
 *
 * @author guozhidong
 */
@Component
public class MyTaskListener implements TaskExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(MyTaskListener.class);
	
	@Override
	public void onTaskStartup(TaskExecution taskExecution) {
		logger.info("starting task:" + taskExecution.getTaskName());
	}

	@Override
	public void onTaskEnd(TaskExecution taskExecution) {
		logger.info("ending task:" + taskExecution.getTaskName());
	}

	@Override
	public void onTaskFailed(TaskExecution taskExecution, Throwable throwable) {
		logger.info("task failed:" + taskExecution.getTaskName());
		logger.error("error msg:" + throwable);
	}

}