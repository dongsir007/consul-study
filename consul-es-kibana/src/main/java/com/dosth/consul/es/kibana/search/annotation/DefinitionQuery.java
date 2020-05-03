package com.dosth.consul.es.kibana.search.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.dosth.consul.es.kibana.search.enums.QueryTypeEnum;

/**
 * 
 * @description 定义查询字段的查询方式
 *
 * @author guozhidong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@Repeatable(DefinitionQueryRepeatable.class)
public @interface DefinitionQuery {
	/**
	 * 查询参数
	 */
	String key() default "";
	
	/**
	 * 查询类型
	 */
	QueryTypeEnum type() default QueryTypeEnum.EQUAL;
	
	/**
	 * 范围查询 from 后缀
	 */
	String fromSuffix() default "From";
	
	/**
	 * 范围查询 to 后缀
	 */
	String toSuffix() default "To";
	
	/**
	 * 多个字段分隔符
	 */
	String separator() default ",";
	
	/**
	 * 指定对象的哪个字段将应用于查询映射
	 * 例如:
	 * 同一个文档下有多个user对象,对象名分别为createUser、updateUser、该User对象属性有name等字段,
	 * 如果要根据查询createUser的name来进行查询
	 * 则可以这样定义DefinitionQuery: quertField = cName, mapped = createdUser.name
	 * 
	 * @return 映射的实体字段路径
	 */
	String mapped() default "";
	
	/**
	 * 嵌套查询的path
	 * @return path
	 */
	String nestedPath() default "";
}