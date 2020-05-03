package com.dosth.consul.es.kibana.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dosth.consul.es.kibana.init.InitIndexService;
import com.dosth.consul.es.kibana.response.ResponseResult;

@RestController
@RequestMapping("/index")
public class IndexController extends BaseController {

	@Resource
	private InitIndexService initIndexService;
	
	@GetMapping("/init")
	public ResponseResult<Object> init() {
		try {
			return ResponseResult.success(initIndexService.initIndex());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
