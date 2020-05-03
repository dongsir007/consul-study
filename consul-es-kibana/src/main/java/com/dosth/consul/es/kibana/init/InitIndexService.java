package com.dosth.consul.es.kibana.init;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import com.dosth.consul.es.kibana.config.properties.IndexEntity;
import com.dosth.consul.es.kibana.model.IndexConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @description 索引服务
 *
 * @author guozhidong
 */
@Service
@Slf4j
public class InitIndexService {
	@Resource
	private IndexEntity indexEntity;
	@Resource
	private ElasticsearchTemplate elasticsearchTemplate;
	
	/**
	 * 初始化索引
	 * @return true 创建成功 其他 失败
	 * @throws ClassNotFoundException 
	 */
	public Object initIndex() throws ClassNotFoundException {
		List<String> documentPath = this.indexEntity.getConfigs().stream().map(IndexConfig::getDocumentPath).collect(Collectors.toList());
		for (String path : documentPath) {
			try {
				Class<?> clazz = Class.forName(path);
				if (!this.elasticsearchTemplate.indexExists(clazz.newInstance().getClass())) {
					log.info("创建索引,clazz:{}", clazz);
					this.elasticsearchTemplate.createIndex(clazz.newInstance().getClass());
					log.info("创建索引SUCCESS, clazz:{}", clazz);
				}
				log.info("创建Mapping映射, clazz:{}", clazz);
                this.elasticsearchTemplate.putMapping(clazz.newInstance().getClass());
                log.info("创建Mapping映射SUCCESS, clazz:{}", clazz);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				 log.error("未加载到索引文件", e);
                 throw new ClassNotFoundException("未加载到索引文件,ClassPath:" + path);
			}
		}
		return true;
	}
}
