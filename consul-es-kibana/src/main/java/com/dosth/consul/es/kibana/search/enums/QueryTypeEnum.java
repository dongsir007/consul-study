package com.dosth.consul.es.kibana.search.enums;

/**
 * 
 * @description 查询类型
 *
 * @author guozhidong
 */
public enum QueryTypeEnum {
	/**
	 * 等于
	 */
	EQUAL,
	/**
	 * 忽略大小写相等
	 */
	EQU_IGNORE_CASE,
	/**
	 * 范围
	 */
	RANGE,
	/**
	 * in
	 */
	IN,
	/**
	 * 忽略
	 */
	IGNORE,
	/**
	 * 搜索
	 */
	FULLTEXT,
	/**
	 * 匹配 和q搜索区分开
	 */
	MATCH,
	/**
	 * 模糊查询
	 */
	FUZZY,
	/**
	 * and
	 */
	AND,
	/**
	 * 多个查询字段匹配上一个即符合条件
	 */
	SHOULD,
	/**
	 * 前缀查询
	 */
	PREFIX,
	/**
	 * 聚合
	 */
	AGGREGATION;
}