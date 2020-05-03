package com.dosth.stream.receiver.service.impl;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.dosth.stream.receiver.service.ReceiveMsg;

@Component
@EnableBinding(Sink.class)
public class ReceiveMsgImpl implements ReceiveMsg {

	@Override
	@StreamListener(Sink.INPUT)
	public void receive(Message<Object> message) {
		System.out.println("接收消息:" + message.getPayload());
	}
}
