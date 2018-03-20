package com.demo.elasticsearch;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class Elactis {
	public static void main(String[] args) throws IOException {
		
//		
		Settings settings = Settings.builder() 
                .put("cluster.name", "my-application") 
                .put("node.name","node-1").build();
		TransportClient client = new PreBuiltTransportClient(settings)  //
        .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
		
		
		IndexResponse response =client.prepareIndex("xue", "str", "2")
	            .setSource(XContentFactory.jsonBuilder()
	                    .startObject()
	                    .field("user", "kimchy")
	                    .field("postDate", new Date())
	                    .field("message", "trying out Elasticsearch")
	                .endObject()
	                    )
	            .get();
	        System.out.println("索引名称："+response.getIndex());
	        System.out.println("类型："+response.getType());
	        System.out.println("文档ID："+response.getId()); // ��һ��ʹ����1
	        System.out.println("当前实例状态："+"---");
	}
}
