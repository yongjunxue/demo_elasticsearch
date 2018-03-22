package com.demo.elasticsearch.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import com.demo.elasticsearch.pojo.User;

@Service
public class MyUserService {
	private static ElasticsearchTemplate template;
	@SuppressWarnings("resource")
	public static void testTemplate(){
		ClassPathXmlApplicationContext con=new ClassPathXmlApplicationContext("classpath:elasticsearch.xml");
		@SuppressWarnings("unused")
		User u=(User) con.getBean("user");
		template= (ElasticsearchTemplate) con.getBean("elasticsearchTemplate");
		Map<String, String> settings = new HashMap<String, String>();
//		settings.put("index.number_of_shards", String.valueOf(2));// 设置索引的分片数
//		settings.put("index.number_of_replicas", String.valueOf(1));// 设置索引的副本数
		template.createIndex("xx321");
//		template.createIndex("xx321", settings);
		
	}
	public static void main(String[] args) {
		testTemplate();
	}
	
}
/**

Description	Resource	Path	Location	Type
The project was not built since its build path is incomplete. Cannot find the class file for org.elasticsearch.search.suggest.SuggestBuilder$SuggestionBuilder. Fix the build path then try building this project	myelasticsearch		Unknown	Java Problem
Description	Resource	Path	Location	Type
The type org.elasticsearch.search.suggest.SuggestBuilder$SuggestionBuilder cannot be resolved. It is indirectly referenced from required .class files	MyUserService.java	/myelasticsearch/src/main/java/com/demo/elasticsearch/service	line 1	Java Problem

*/