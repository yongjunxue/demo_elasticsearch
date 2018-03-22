package com.demo.elasticsearch;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;




public class MyElasticsearchTemplate{
	
	private static String clusterName="elasticsearch";//跟elasticsearch.yml中的cluster.name一样
	private static String nodeName="node-1";//跟elasticsearch.yml中的node.name一样
	private static String ip="127.0.0.1"; 
	private static int port=9300; //默认是这个端口
	
	private TransportClient client=null;
	private static ElasticsearchTemplate template=null;
	
	public MyElasticsearchTemplate(){
		super();
		Settings settings = Settings.builder() 
                .put("cluster.name", clusterName) 
//                .put("node.name",nodeName)
                .build();
		try {
			client = new PreBuiltTransportClient(settings)  //
			.addTransportAddress(new TransportAddress(InetAddress.getByName(ip), port));
//			.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), 9301));
		} catch (Exception e) {
			e.printStackTrace();
		}
		template=new ElasticsearchTemplate(client);
		
	}
	
	public static void main(String[] args) {
		MyElasticsearchTemplate m=new MyElasticsearchTemplate();
		Map<String, String> settings = new HashMap<String, String>();
//		settings.put("index.number_of_shards", String.valueOf(2));// 设置索引的分片数
//		settings.put("index.number_of_replicas", String.valueOf(1));// 设置索引的副本数
		template.createIndex("xx321");
		
	}
}
