package com.dosth.consul.es.kibana.docindex.repo;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.dosth.consul.es.kibana.document.StoreDocument;

/**
 * 
 * @description 门店repository
 *
 * @author guozhidong
 */
public interface StoreRepository extends ElasticsearchRepository<StoreDocument, java.lang.String> {

}
