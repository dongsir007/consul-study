package com.dosth.consul.es.kibana.model;

import lombok.Data;

@Data
public class IndexConfig {
	/**
	 * 文档编码
	 */
	private String docCode;
	/**
	 * 索引名称
	 */
	private String indexName;
	/**
	 * 索引类型
	 */
	private String type;
	/**
	 * 索引文档路径
	 */
	private String documentPath;
}
