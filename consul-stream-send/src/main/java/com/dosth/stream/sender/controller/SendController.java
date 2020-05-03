package com.dosth.stream.sender.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dosth.stream.sender.service.SendMsg;

@RestController
public class SendController {

	@Autowired
	private SendMsg sendMsg;
	
	@GetMapping("/send")
	public String msgSend(String msg) {
		this.sendMsg.send(msg);
		return "success";
	}
}