package com.demo.elasticsearch;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.alibaba.fastjson.JSONObject;

public class Elastic {
	
	private static String clusterName="my-application";//跟elasticsearch.yml中的cluster.name一样
	private static String nodeName="node-1";//跟elasticsearch.yml中的node.name一样
	private static String ip="localhost"; 
	private static int port=9300; //默认是这个端口
	
	private static TransportClient client=null;
	
	public Elastic(){
		Settings settings = Settings.builder() 
                .put("cluster.name", clusterName) 
                .put("node.name",nodeName).build();
		try {
			client = new PreBuiltTransportClient(settings)  //
			.addTransportAddress(new TransportAddress(InetAddress.getByName(ip), port));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void addIndex1() throws IOException{
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
	
	public void addIndex2()throws Exception{
        String json = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
            "}";
         
        IndexResponse response =client.prepareIndex("weibo", "tweet")
            .setSource(json,XContentType.JSON)
            .get();
        System.out.println("索引名称："+response.getIndex());
        System.out.println("类型："+response.getType());
        System.out.println("文档ID："+response.getId()); // 第一次使用是1
        System.out.println("当前实例状态："+response.status());
    }
	
	public void addIndex3()throws Exception{
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("user","kimchy");
        json.put("postDate",new Date());
        json.put("message","trying out Elasticsearch");
         
        IndexResponse response =client.prepareIndex("qq", "tweet")
            .setSource(json)
            .get();
        System.out.println("索引名称："+response.getIndex());
        System.out.println("类型："+response.getType());
        System.out.println("文档ID："+response.getId()); // 第一次使用是1
        System.out.println("当前实例状态："+response.status());
	}
	
	public void addIndex4()throws Exception{
		JSONObject jsonObject=new JSONObject();
        jsonObject.put("user", "kimchy");
        jsonObject.put("postDate", "1989-11-11");
        jsonObject.put("message", "trying out Elasticsearch");
         
        IndexResponse response =client.prepareIndex("qq", "tweet")
            .setSource(jsonObject.toString(),XContentType.JSON)
            .get();
        System.out.println("索引名称："+response.getIndex());
        System.out.println("类型："+response.getType());
        System.out.println("文档ID："+response.getId()); // 第一次使用是1
        System.out.println("当前实例状态："+response.status());
	} 
	
	public void getData()throws Exception{
		GetResponse getResponse=client.prepareGet("twitter", "tweet", "1").get();
	    System.out.println(getResponse.getSourceAsString());
	} 
	
	public void updateData(){
		JSONObject jsonObject=new JSONObject();
	    jsonObject.put("user", "薛");
	    jsonObject.put("postDate", "1989-11-11");
	    jsonObject.put("message", "学习Elasticsearch");
	     
	    UpdateResponse response = client.prepareUpdate("twitter", "tweet", "1").setDoc(jsonObject.toString(),XContentType.JSON).get();
	    System.out.println("索引名称："+response.getIndex());
	    System.out.println("类型："+response.getType());
	    System.out.println("文档ID："+response.getId()); // 第一次使用是1
	    System.out.println("当前实例状态："+response.status());
	}
	
	public void deleteData(){
	    DeleteResponse response=client.prepareDelete("twitter", "tweet", "1").get();
	    System.out.println("索引名称："+response.getIndex());
	    System.out.println("类型："+response.getType());
	    System.out.println("文档ID："+response.getId()); // 第一次使用是1
	    System.out.println("当前实例状态："+response.status());
	}
	
	public static void main(String[] args) throws Exception {
		Elastic e=new Elastic();
//		e.addIndex4();
//		e.updateData();
//		e.deleteData();
		e.getData();
	}
}
