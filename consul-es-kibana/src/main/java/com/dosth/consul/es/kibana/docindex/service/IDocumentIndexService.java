package com.dosth.consul.es.kibana.docindex.service;

import java.util.Map;

public interface IDocumentIndexService {
	/**
	 * 加载扩展数据
	 * @param sourceData 原数据
	 */
	void loadExpansion(Map sourceData);
}
