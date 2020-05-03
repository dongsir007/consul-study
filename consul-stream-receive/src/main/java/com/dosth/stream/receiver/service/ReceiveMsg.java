package com.dosth.stream.receiver.service;

import org.springframework.messaging.Message;

public interface ReceiveMsg {
	public void receive(Message<Object> message);
}
