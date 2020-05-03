package com.dosth.consul.es.kibana.document;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.dosth.consul.es.kibana.document.store.StoreBaseInfo;
import com.dosth.consul.es.kibana.document.store.StoreTags;
import com.dosth.consul.es.kibana.search.annotation.DefinitionQuery;
import com.dosth.consul.es.kibana.search.enums.QueryTypeEnum;

import lombok.Data;

/**
 * 
 * @description 门店Document
 *
 * @author guozhidong
 */
@Document(indexName = "store", type = "base")
@Data
@DefinitionQuery(key = "page", type = QueryTypeEnum.IGNORE)
@DefinitionQuery(key = "size", type = QueryTypeEnum.IGNORE)
@DefinitionQuery(key = "q", type = QueryTypeEnum.FULLTEXT)
public class StoreDocument {
	@Id
	@DefinitionQuery(key = "id", type = QueryTypeEnum.IN)
	@Field(type = FieldType.Keyword)
	private String id;

	/**
	 * 基础信息
	 */
	@Field(type = FieldType.Object)
	private StoreBaseInfo baseInfo;

	/**
	 * 标签
	 */
	@Field(type = FieldType.Nested)
	@DefinitionQuery(key = "tagCode", mapped = "tags.key", type = QueryTypeEnum.IN)
	@DefinitionQuery(key = "tagValue", mapped = "tags.value", type = QueryTypeEnum.AND)
	@DefinitionQuery(key = "_tagValue", mapped = "tags.value", type = QueryTypeEnum.IN)
	private List<StoreTags> tags;
}