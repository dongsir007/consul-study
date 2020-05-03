package com.dosth.stream.sender.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.dosth.stream.sender.service.SendMsg;

@Service
@EnableBinding(Source.class)
public class SendMsgImpl implements SendMsg {

	@Autowired
	private MessageChannel output;
	
	@Override
	public void send(Object obj) {
		this.output.send(MessageBuilder.withPayload(obj).build());
	}
}
