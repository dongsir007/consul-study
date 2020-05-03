package com.dosth.consul.es.kibana.model;

import com.dosth.consul.es.kibana.search.enums.QueryTypeEnum;

import lombok.Builder;
import lombok.Data;

@Data
public class FieldDefinition {
	@Builder
	public FieldDefinition() {
	}
	@Builder
	public FieldDefinition(String key, QueryTypeEnum queryType, String queryField, String fromSuffix, String toSuffix,
			String separator, String nestedPath) {
		this.key = key;
		this.queryType = queryType;
		this.queryField = queryField;
		this.fromSuffix = fromSuffix;
		this.toSuffix = toSuffix;
		this.separator = separator;
		this.nestedPath = nestedPath;
	}
	/**
	 * 查询参数
	 */
	private String key;
	/**
	 * 查询类型
	 */
	private QueryTypeEnum queryType;
	/**
	 * 查询参数对应文档中的字段
	 */
	private String queryField;
	/**
	 * from 后缀
	 */
	private String fromSuffix;
	/**
	 * to后缀
	 */
	private String toSuffix;
	/**
	 * 分割符
	 */
	private String separator;
	/**
	 * 嵌套查询的路径
	 */
	private String nestedPath;
}
