package com.dosth.consul.es.kibana.search.mapping;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.dosth.consul.es.kibana.ConsulEsKibanaApp;
import com.dosth.consul.es.kibana.model.FieldDefinition;
import com.dosth.consul.es.kibana.model.Key;
import com.dosth.consul.es.kibana.search.annotation.DefinitionQuery;
import com.dosth.consul.es.kibana.search.annotation.DefinitionQueryRepeatable;

public class KeyMapping {
	private static final String BOOTSTRAP_PATH = ConsulEsKibanaApp.class.getPackage().getName();
	
	/**
	 * 字段映射
	 * @param clazz
	 * @return
	 */
	public static Map<Key, FieldDefinition> mapping(Class<?> clazz) {
		Map<Key, FieldDefinition> mappings = mapping(clazz.getDeclaredFields(), "");
		mappings.putAll(typeMapping(clazz));
		return mappings;
	}
	
	/**
	 * 字段映射
	 * @param fields
	 * @param parentField
	 * @return
	 */
	public static Map<Key, FieldDefinition> mapping(Field[] fields, String parentField) {
		Map<Key, FieldDefinition> mappings = new HashMap<>();
		String nestedPath = null;
		DefinitionQuery[] definitionQueries;
		for (Field field : fields) {
			org.springframework.data.elasticsearch.annotations.Field fieldAnnotation = field.getAnnotation(org.springframework.data.elasticsearch.annotations.Field.class);
			if (Objects.nonNull(fieldAnnotation) && FieldType.Nested.equals(fieldAnnotation.type())) {
				nestedPath = parentField + field.getName();
			}
			definitionQueries = field.getAnnotationsByType(DefinitionQuery.class);
			// 如果属性非BOOTSTRAP_PATH包下的类,说明属性为基础字段 即跳出循环,否则递归调用mapping
			if (!field.getType().getName().startsWith(BOOTSTRAP_PATH)) {
				for (DefinitionQuery query : definitionQueries) {
					buildMapping(parentField, mappings, field, nestedPath, query);
				}
			} else {
				for (DefinitionQuery query : definitionQueries) {
					if (StringUtils.isNotBlank(query.mapped())) {
						buildMapping(parentField, mappings, field, nestedPath, query);
					}
				}
				mappings.putAll(mapping(field.getType().getDeclaredFields(), parentField + field.getName() + "."));
			}
		}
		return mappings;
	}
	
	/**
	 * 构建mapping
	 * @param parentField 父级字段名
	 * @param mappings mapping
	 * @param field 字段
	 * @param nestedPath 默认嵌套路径
	 * @param query 字段定义
	 */
	private static void buildMapping(String parentField, Map<Key, FieldDefinition> mappings, Field field, String nestedPath, DefinitionQuery query) {
		FieldDefinition definition;
		nestedPath = StringUtils.isNotBlank(query.nestedPath()) ? query.nestedPath() : nestedPath;
		String key = StringUtils.isBlank(query.key()) ? field.getName() : query.key();
		String fieldName = StringUtils.isBlank(query.mapped()) ? field.getName() : query.mapped();
		switch (query.type()) {
		case RANGE:
			buildRange(parentField, mappings, query, key, fieldName);
			break;
		default:
			definition = FieldDefinition.builder()
				.key(key)
				.queryField(parentField + fieldName)
				.queryType(query.type())
				.separator(query.separator())
				.nestedPath(nestedPath)
				.build();
			mappings.put(new Key(key), definition);
			break;
		}
	}
	
	/**
	 * 对象映射
	 * @param clazz
	 * @return
	 */
	public static Map<Key, FieldDefinition> typeMapping(Class<?> clazz) {
		DefinitionQueryRepeatable repeatable = (DefinitionQueryRepeatable) clazz.getAnnotation(DefinitionQueryRepeatable.class);
		Map<Key, FieldDefinition> mappings = new HashMap<>();
		String key;
		FieldDefinition definition;
		for (DefinitionQuery query : repeatable.value()) {
			key = query.key();
			switch (query.type()) {
			case RANGE:
				buildRange("", mappings, query, key, query.mapped());
				break;
			default:
				definition = FieldDefinition.builder()
					.key(key)
					.queryField(key)
					.queryType(query.type())
					.separator(query.separator())
					.nestedPath(query.nestedPath())
					.build();
				mappings.put(new Key(key), definition);
				break;
			}
		}
		return mappings;
	}
	
	/**
	 * 构建范围查询
	 * @param parentField 父级字段名
	 * @param mappings mapping
	 * @param query 字段定义
	 * @param key 入参查询字段
	 * @param fieldName 索引文档中字段名
	 */
	private static void buildRange(String parentField, Map<Key, FieldDefinition> mappings, DefinitionQuery query, String key, String fieldName) {
		FieldDefinition definition;
		String queryField = parentField + fieldName;
		String rangeKeyFrom = key + query.fromSuffix();
		String rangeKeyTo = key + query.toSuffix();
		
		definition = FieldDefinition.builder()
				.key(rangeKeyFrom)
				.queryField(queryField)
				.queryType(query.type())
				.fromSuffix(query.fromSuffix())
				.toSuffix(query.toSuffix())
				.build();
		mappings.put(new Key(rangeKeyFrom), definition);
		
		definition = FieldDefinition.builder()
				.key(rangeKeyTo)
				.queryField(queryField)
				.queryType(query.type())
				.fromSuffix(query.fromSuffix())
				.toSuffix(query.toSuffix())
				.build();
		mappings.put(new Key(rangeKeyTo), definition);
	}
}