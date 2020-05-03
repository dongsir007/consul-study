package com.dosth.consul.es.kibana;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.dosth.consul.es.kibana.document.StoreDocument;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	@Test
	public void test() {
		System.err.println(this.elasticsearchTemplate.createIndex(StoreDocument.class));
		System.out.println(this.elasticsearchTemplate.putMapping(StoreDocument.class));
	}
}