package com.dosth.consul.es.kibana.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {
	@Autowired
	protected HttpServletRequest request;
}
