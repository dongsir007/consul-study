package com.dosth.consul.es.kibana.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyBucket {
	private Object key;
	private Long docCount;
}