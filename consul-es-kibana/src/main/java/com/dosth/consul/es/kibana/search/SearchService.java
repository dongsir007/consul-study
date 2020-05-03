package com.dosth.consul.es.kibana.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dosth.consul.es.kibana.global.Global;
import com.dosth.consul.es.kibana.model.FieldDefinition;
import com.dosth.consul.es.kibana.model.Key;
import com.dosth.consul.es.kibana.model.MyBucket;
import com.dosth.consul.es.kibana.search.enums.QueryTypeEnum;

@Service
public class SearchService {

	@Resource
	private ElasticsearchTemplate elasticsearchTemplate;
	
	/**
	 * 通用查询
	 * @param params 查询入参
	 * @param indexName 索引名称
	 * @param type 索引类型
	 * @param defaultSort 默认排序
	 * @param keyMappings 字段映射
	 * @param keyMappingsMap 索引对应字段映射
	 * @return
	 */
	protected Page<Map> commonSearch(Map<String, String> params, String indexName, String type, String defaultSort,
			Map<Key, FieldDefinition> keyMappings,
			Map<String, Map<Key, FieldDefinition>> keyMappingsMap) {
		SearchQuery query = buildSearchQuery(params, indexName, type, defaultSort, keyMappings, keyMappingsMap);
		return this.elasticsearchTemplate.queryForPage(query, Map.class);
	}
	
	protected List<?> aggregate(Map<String, String> params, String indexName, String type,
			Map<Key, FieldDefinition> keyMappings,
			Map<String, Map<Key, FieldDefinition>> keyMappingsMap) {
		SearchQuery searchQuery = this.buildSearchQuery(params, indexName, type, null, keyMappings, keyMappingsMap);
		AggregatedPage<Map> aggregatedPage = this.elasticsearchTemplate.queryForPage(searchQuery, Map.class);
		return aggregatedPage.getAggregations().asList().stream().map(aggregation -> {
			MultiBucketsAggregation bucketsAggregation = (MultiBucketsAggregation) aggregation;
			return Collections.singletonMap(aggregation.getName(), bucketsAggregation.getBuckets()
					.stream()
					.map(bucket -> new MyBucket(bucket.getKey(), bucket.getDocCount()))
					.collect(Collectors.toList())
			);
		}).collect(Collectors.toList());
	}
	
	/**
	 * 数量通用查询
	 * @param params 查询入参
	 * @param indexName 索引名称
	 * @param type 索引类型
	 * @param defaultSort 默认排序
	 * @param keyMappings 字段映射
	 * @param keyMappingsMap 索引对应字段映射
	 * @return
	 */
	protected long count(Map<String, String> params, String indexName, String type, String defaultSort, Map<Key, FieldDefinition> keyMappings,
			Map<String, Map<Key, FieldDefinition>> keyMappingsMap) {
		SearchQuery searchQuery = this.buildSearchQuery(params, indexName, type, defaultSort, keyMappings, keyMappingsMap);
		return this.elasticsearchTemplate.count(searchQuery);
	}
	
	/**
	 * 根据Id获取索引
	 * @param id Id
	 * @param indexName 索引名
	 * @param type 索引类型
	 * @return 索引
	 */
	protected Map<String, Object> get(String id, String indexName, String type) {
		return this.elasticsearchTemplate.getClient()
				.prepareGet(indexName, type, id)
				.execute()
				.actionGet()
				.getSourceAsMap();
	}

	/**
	 * 根据定义的查询字段封装查询语句
	 * @param params 查询入参
	 * @param indexName 索引名称
	 * @param type 索引类型
	 * @param defaultSort 默认排序
	 * @param keyMappings 字段映射
	 * @param keyMappingsMap 索引对应字段映射
	 * @return SearchQuery
	 */
	private SearchQuery buildSearchQuery(Map<String, String> params, String indexName, String type, String defaultSort,
			Map<Key, FieldDefinition> keyMappings, Map<String, Map<Key, FieldDefinition>> keyMappingsMap) {
		NativeSearchQueryBuilder builder = buildSearchField(params, indexName, type, keyMappings, keyMappingsMap);
		String sortField = params.getOrDefault(Global.SORT, defaultSort);
		if (StringUtils.isNotBlank(sortField)) {
			String[] sorts = sortField.split(Global.SPLIT_FLAG_COMMA);
			handleQuerySort(builder, sorts);
		}
		return builder.build();
	}

	/**
	 * 根据定义的查询字段封装查询语句
	 * @param params 查询入参
	 * @param indexName 索引名称
	 * @param type 索引类型
	 * @param keyMappings 字段映射
	 * @param keyMappingsMap 索引对应字段映射
	 * @return NativeSearchQueryBuilder
	 */
	private NativeSearchQueryBuilder buildSearchField(Map<String, String> params, String indexName, String type,
			Map<Key, FieldDefinition> keyMappings, Map<String, Map<Key, FieldDefinition>> keyMappingsMap) {
		int page = Integer.valueOf(params.getOrDefault(Global.PAGE, "0"));
		int size = Integer.valueOf(params.getOrDefault(Global.SIZE, "10"));
		
		AtomicBoolean matchSearch = new AtomicBoolean(false);
		
		String q = params.get(Global.Q);
		String missingFields = params.get(Global.MISSING);
		String existsFields = params.get(Global.EXISTS);
		
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		BoolQueryBuilder boolFilterBuilder = QueryBuilders.boolQuery();
		
		Map<String, BoolQueryBuilder> nestedMustMap = new HashMap<>();
		Map<String, BoolQueryBuilder> nestedMustNotMap = new HashMap<>();
		List<String> fullTextFieldList = new ArrayList<>();
		
		NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder()
				.withIndices(params.getOrDefault(Global.INDEX_NAME, indexName))
				.withTypes(params.getOrDefault(Global.INDEX_TYPE, type))
				.withPageable(PageRequest.of(page, size));
		
		String fields = params.get(Global.FIELDS);
		if (Objects.nonNull(fields)) {
			builder.withFields(fields.split(Global.SPLIT_FLAG_COMMA));
		}
		keyMappingsMap.getOrDefault(params.getOrDefault(Global.INDEX_NAME, indexName), keyMappings)
			.entrySet()
			.stream()
			.filter(m -> m.getValue().getQueryType().equals(QueryTypeEnum.FULLTEXT)
				|| !m.getValue().getQueryType().equals(QueryTypeEnum.IGNORE)
				&& params.get(m.getKey().toString()) != null)
			.forEach(m -> {
				String k = m.getKey().toString();
				FieldDefinition definition = m.getValue();
				String queryValue = params.get(k);
				QueryTypeEnum queryType = definition.getQueryType();
				String queryName = definition.getQueryField();
				String nestedPath = definition.getNestedPath();
				BoolQueryBuilder nestedMustBoolQuery = null;
				BoolQueryBuilder nestedMustNotBoolQuery = null;
				boolean nested = false;
				if (StringUtils.isNotBlank(nestedPath)) {
					nested = true;
					if (nestedMustMap.containsKey(nestedPath)) {
						nestedMustBoolQuery = nestedMustMap.get(nestedPath);
					} else {
						nestedMustBoolQuery = QueryBuilders.boolQuery();
					}
					if (nestedMustNotMap.containsKey(nestedPath)) {
						nestedMustNotBoolQuery = nestedMustNotMap.get(nestedPath);
					} else {
						nestedMustNotBoolQuery = QueryBuilders.boolQuery();
					}
				}
				switch (queryType) {
				case RANGE:
					RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder(queryName);
					if (k.endsWith(definition.getFromSuffix())) {
						rangeQueryBuilder.from(queryValue);
					} else {
						rangeQueryBuilder.to(queryValue);
					}
					boolFilterBuilder.must(rangeQueryBuilder);
					break;
				case FUZZY:
					if (nested) {
						if (k.startsWith(Global.NON_FLAG)) {
							nestedMustBoolQuery.mustNot(QueryBuilders.wildcardQuery(queryName, queryValue));
						} else {
							nestedMustBoolQuery.filter(QueryBuilders.wildcardQuery(queryName, StringUtils.wrapIfMissing(queryValue, Global.WILDCARD)));
						}
					} else {
						if (k.startsWith(Global.NON_FLAG)) {
							boolFilterBuilder.mustNot(QueryBuilders.wildcardQuery(queryName, queryValue));
						} else {
							boolFilterBuilder.filter(QueryBuilders.wildcardQuery(queryName, StringUtils.wrapIfMissing(queryValue, Global.WILDCARD)));
						}
					}
					break;
				case PREFIX:
					boolFilterBuilder.filter(QueryBuilders.prefixQuery(queryName, queryValue));
					break;
				case AND:
					if (nested) {
						for (String and : queryValue.split(definition.getSeparator())) {
							nestedMustBoolQuery.must(QueryBuilders.termQuery(queryName, and));
						}
					} else {
						for (String and : queryValue.split(definition.getSeparator())) {
							boolFilterBuilder.must(QueryBuilders.termQuery(queryName, and));
						}
					}
					break;
				case IN:
					String inQuerySeparator = definition.getSeparator();
					if (nested) {
						buildIn(k, queryValue, queryName, nestedMustBoolQuery, inQuerySeparator, nestedMustNotBoolQuery);
					} else {
						buildIn(k, queryValue, queryName, boolFilterBuilder, inQuerySeparator);
					}
					break;
				case SHOULD:
					boolFilterBuilder.should(QueryBuilders.wildcardQuery(queryName, StringUtils.wrapIfMissing(queryValue, Global.WILDCARD)));
					break;
				case FULLTEXT:
					if (!Global.Q.equalsIgnoreCase(queryName)) {
						fullTextFieldList.add(queryName);
					}
					break;
				case MATCH:
					boolQueryBuilder.must(QueryBuilders.matchQuery(queryName, queryValue));
					matchSearch.set(true);
					break;
				case EQU_IGNORE_CASE:
					boolFilterBuilder.must(QueryBuilders.termQuery(queryName, queryValue.toLowerCase()));
					break;
				case AGGREGATION:
					builder.addAggregation(AggregationBuilders.terms(definition.getKey())
						.field(queryName)
						.showTermDocCountError(true));
					break;
				default:
					boolFilterBuilder.must(QueryBuilders.termQuery(queryName, queryValue));
					break;
				}
				if (nested) {
					if (nestedMustBoolQuery.hasClauses()) {
						nestedMustMap.put(nestedPath, nestedMustBoolQuery);
					}
					if (nestedMustNotBoolQuery.hasClauses()) {
						nestedMustNotMap.put(nestedPath, nestedMustNotBoolQuery);
					}
				}
			});
		if (StringUtils.isNotBlank(q)) {
			MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(q);
			fullTextFieldList.forEach(multiMatchQueryBuilder::field);
			boolQueryBuilder.should(multiMatchQueryBuilder);
		}
		if (StringUtils.isNotBlank(q) || matchSearch.get()) {
			builder.withSort(SortBuilders.scoreSort().order(SortOrder.DESC));
		}
		if (StringUtils.isNotBlank(missingFields)) {
			for (String miss : missingFields.split(Global.SPLIT_FLAG_COMMA)) {
				boolFilterBuilder.mustNot(QueryBuilders.existsQuery(miss));
			}
		}
		if (StringUtils.isNotBlank(existsFields)) {
			for (String exists : existsFields.split(Global.SPLIT_FLAG_COMMA)) {
				boolFilterBuilder.must(QueryBuilders.existsQuery(exists));
			}
		}
		if (!CollectionUtils.isEmpty(nestedMustMap)) {
			for (Entry<String, BoolQueryBuilder> entry : nestedMustMap.entrySet()) {
				if (StringUtils.isBlank(entry.getKey())) {
					continue;
				}
				boolFilterBuilder.must(QueryBuilders.nestedQuery(entry.getKey(), entry.getValue(), ScoreMode.None));
			}
		}
		if (!CollectionUtils.isEmpty(nestedMustNotMap)) {
			for (Entry<String, BoolQueryBuilder> entry : nestedMustNotMap.entrySet()) {
				if (StringUtils.isBlank(entry.getKey())) {
					continue;
				}
				boolFilterBuilder.mustNot(QueryBuilders.nestedQuery(entry.getKey(), entry.getValue(), ScoreMode.None));
			}
		}
		
		builder.withFilter(boolFilterBuilder);
		builder.withQuery(boolQueryBuilder);
		return builder;
	}
	
	private void buildIn(String k, String queryValue, String queryName, BoolQueryBuilder builder, String separator) {
		buildIn(k, queryValue, queryName, builder, separator, null);
	}
	
	private void buildIn(String k, String queryValue, String queryName, BoolQueryBuilder builder, String separator, BoolQueryBuilder nestedMustNotBoolQuery) {
		if (queryValue.contains(separator)) {
			if (k.startsWith(Global.NON_FLAG)) {
				if (Objects.nonNull(nestedMustNotBoolQuery)) {
					nestedMustNotBoolQuery.must(QueryBuilders.termQuery(queryName, Arrays.asList(queryValue.split(separator))));
				} else {
					builder.mustNot(QueryBuilders.termQuery(queryName, Arrays.asList(queryValue.split(separator))));
				}
			} else {
				builder.must(QueryBuilders.termQuery(queryName, Arrays.asList(queryValue.split(separator))));
			}
		} else {
			if (k.startsWith(Global.NON_FLAG)) {
				if (Objects.nonNull(nestedMustNotBoolQuery)) {
					nestedMustNotBoolQuery.must(QueryBuilders.termQuery(queryName, queryValue));
				} else {
					builder.mustNot(QueryBuilders.termQuery(queryName, queryValue));
				}
			} else {
				builder.must(QueryBuilders.termQuery(queryName, queryValue));
			}
		}
	}
	
	/**
	 * 处理排序
	 * @param builder
	 * @param sorts 排序字段
	 */
	private void handleQuerySort(NativeSearchQueryBuilder builder, String[] sorts) {
		for (String sort : sorts) {
			this.sortBuilder(builder, sort);
		}
	}

	private void sortBuilder(NativeSearchQueryBuilder builder, String sort) {
		switch (sort.charAt(0)) {
		case '-': // 字段前有-:倒序排序
			builder.withSort(SortBuilders.fieldSort(sort.substring(1)).order(SortOrder.DESC));
			break; // 字段前有+:正序排序
		case '+':
			builder.withSort(SortBuilders.fieldSort(sort.substring(1)).order(SortOrder.ASC));
			break;
		default:
			builder.withSort(SortBuilders.fieldSort(sort.trim()).order(SortOrder.ASC));
			break;
		}
	}
	
	/**
	 * 获取一个符合查询条件的数据
	 * @param filterBuilder 查询条件
	 * @param indexName 索引名
	 * @param type 索引类型
	 * @return Map
	 */
	protected Map<String, Object> getOne(TermQueryBuilder filterBuilder, String indexName, String type) {
		final SearchResponse searchResponse = this.elasticsearchTemplate.getClient()
				.prepareSearch(indexName)
				.setTypes(type)
				.setPostFilter(filterBuilder)
				.setSize(1)
				.get();
		final long total = searchResponse.getHits().getTotalHits();
		if (total > 0) {
			return searchResponse.getHits().getAt(0).getSourceAsMap();
		}
		return null;
	}
}