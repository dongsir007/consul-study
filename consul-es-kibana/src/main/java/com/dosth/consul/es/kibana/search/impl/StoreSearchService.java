package com.dosth.consul.es.kibana.search.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.dosth.consul.es.kibana.config.properties.IndexEntity;
import com.dosth.consul.es.kibana.document.StoreDocument;
import com.dosth.consul.es.kibana.model.FieldDefinition;
import com.dosth.consul.es.kibana.model.IndexConfig;
import com.dosth.consul.es.kibana.model.Key;
import com.dosth.consul.es.kibana.search.SearchService;
import com.dosth.consul.es.kibana.search.mapping.KeyMapping;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StoreSearchService extends SearchService {

	private Map<Key, FieldDefinition> keyMappings;

	private Map<String, Map<Key, FieldDefinition>> keyMappingsMap;

	@Resource
	private IndexEntity indexEntity;

	// 默认排序
	private static final String DEFAULT_SORT = "-baseInfo.createdTime";

	private static final String DOC_CODE = IndexEntity.DOC_CODE_STORE;

	@PostConstruct
	private void init() {
		log.info("mapping queries");
		keyMappingsMap = new HashMap<>();
//		IndexConfig config = indexEntity.getConfigByDocCode(DOC_CODE);
//		log.info("Get Config:{}", config);
//		log.info("Mapping query[clusterName: {}, indexName: {}, indexType: {}]", "my-cluster", config.getIndexName(),
//				config.getType());
		keyMappings = KeyMapping.mapping(StoreDocument.class);
//		keyMappingsMap.put(config.getIndexName(), keyMappings);
		keyMappings.forEach((k, v) -> log.info("Mapped query {}", v));
	} 
	
	/**
     * 查询
     * @param params 查询条件
     * @return page
     */
    public Page<Map> search(Map<String, String> params) {
        IndexConfig config = indexEntity.getConfigByDocCode(DOC_CODE);
        return commonSearch(params, config.getIndexName(), config.getType(), DEFAULT_SORT,
                keyMappings, keyMappingsMap);
    }

    public List aggregate(Map<String, String> params) {
        IndexConfig config = indexEntity.getConfigByDocCode(DOC_CODE);
        return aggregate(params, config.getIndexName(), config.getType(),
                keyMappings, keyMappingsMap);
    }

    /**
     * 查询
     * @param params 查询条件
     * @return page
     */
    public long count(Map<String, String> params) {
        IndexConfig config = indexEntity.getConfigByDocCode(DOC_CODE);
        return count(params, config.getIndexName(), config.getType(), null,
                keyMappings, keyMappingsMap);
    }

    /**
     * 根据ID获取索引
     * @param id ID
     * @return 索引
     */
    public Map get(String id) {
        IndexConfig config = indexEntity.getConfigByDocCode(DOC_CODE);
        return get(id, config.getIndexName(), config.getType());
    }
}
