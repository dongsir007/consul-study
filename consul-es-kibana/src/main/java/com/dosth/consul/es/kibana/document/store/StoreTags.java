package com.dosth.consul.es.kibana.document.store;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;

@Data
public class StoreTags {

	@Field(type = FieldType.Keyword)
	private String key;
	
	@Field(type = FieldType.Keyword)
	private String value;
	
	private String showName;
}