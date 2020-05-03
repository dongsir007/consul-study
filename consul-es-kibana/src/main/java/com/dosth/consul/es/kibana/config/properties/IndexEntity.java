package com.dosth.consul.es.kibana.config.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.dosth.consul.es.kibana.model.IndexConfig;

import lombok.Data;

@ConfigurationProperties("index-entity")
@Data
@Component
public class IndexEntity {

	public static final String DOC_CODE_STORE = "store";
	
	/**
	 * 创建索引的文档配置
	 */
	private List<IndexConfig> configs;
	
	/**
	 * 根据文档编码获取配置信息
	 * @param docCode 文档编码
	 * @return 配置
	 */
	public IndexConfig getConfigByDocCode(String docCode) {
		for (IndexConfig config : configs) {
			if (config.getDocCode().equals(docCode)) {
				return config;
			}
		}
		return null;
	}
}
