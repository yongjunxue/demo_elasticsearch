package com.demo.elasticsearch;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.InternalNumericMetricsAggregation;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.alibaba.fastjson.JSONObject;

/**
 * 1.要在localhost:9300页面看到可视化的数据，需要先启动elasticsearch.bat,然后在elasticsearch-head-master目录下使用命令npm start;
 * 2.一个index只能有一种type。比如，如果存在{index:a,type:int,id:1}，那么插入{index:a,type:long,id:1}时会报错。但是可以有多个id。
 * @author xue
 *
 */
public class Elastic {
	
	private static String clusterName="my-application";//跟elasticsearch.yml中的cluster.name一样
	private static String nodeName="node-1";//跟elasticsearch.yml中的node.name一样
	private static String ip="localhost"; 
	private static int port=9300; //默认是这个端口
	
	private TransportClient client=null;
//	private ElasticsearchTemplate template; //由于版本问题，很多方法都报错，没法用
	
	public Elastic(){
		Settings settings = Settings.builder() 
                .put("cluster.name", clusterName) 
                .put("node.name",nodeName).build();
		try {
			client = new PreBuiltTransportClient(settings)  //
			.addTransportAddress(new TransportAddress(InetAddress.getByName(ip), port));   //TransportAddress    InetSocketTransportAddress
//			template=new ElasticsearchTemplate(client);
//			client=new TransportClientFactoryBean();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void addIndex1() throws IOException{
		IndexResponse response =client.prepareIndex("pl5", "user", "13")
            .setSource(XContentFactory.jsonBuilder()
                    .startObject()
                    .field("name", "十三")
                    .field("money", 1300)
                    .field("postDate", new Date())
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
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user","xue20180321");
        map.put("postDate",new Date());
        map.put("message","trying out Elasticsearch");
        map.put("index.number_of_shards", String.valueOf(2));// 设置索引的分片数
        map.put("index.number_of_replicas", String.valueOf(1));
         
        IndexResponse response =client.prepareIndex("qq", "tweet")
            .setSource(map)
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
	
	/**
	 * 精确查询
	 * @throws Exception
	 */
	public void getData()throws Exception{
		GetResponse getResponse=client.prepareGet("xue*", "1", "1").get();
	    System.out.println(getResponse.getSourceAsString());
	} 
	
	
	/**
	 * 模糊查询及排序
	 * @throws Exception
	 */
	public void getDataList()throws Exception{
		SearchResponse s=client.prepareSearch("xue").addSort("money",SortOrder.ASC).get();
		//SearchResponse s=client.prepareSearch("ue*").addSort("_id",SortOrder.ASC).execute().get();
		//SearchResponse s=client.prepareSearch("ue*").addSort("_id",SortOrder.ASC).execute().actionGet();
		System.out.println(s.toString());
		SearchHits re=s.getHits();
		Iterator<SearchHit> it=re.iterator();
		while(it.hasNext()){
			SearchHit item=it.next();
			System.out.println("id:"+item.getId()+",index:"+item.getIndex()+",type:"+item.getType()+","+item.getSourceAsString());
		}
	} 
	/**
	 * 聚合函数求和-------------按一个条件聚合
	 * @throws Exception
	 */
	public void getDataGroup()throws Exception{
		AggregationBuilder agg=new SumAggregationBuilder("sumMoney").field("money");
		SearchRequestBuilder group=client.prepareSearch("pl*");
		
		group.addAggregation(agg);
		SearchResponse s=group.execute().actionGet();
		
		System.out.println(s.toString());
		Map<String,Aggregation>aggMap=s.getAggregations().asMap();
		InternalSum  teamAgg=(InternalSum ) aggMap.get("sumMoney");  
		System.out.println(teamAgg.toString());
		System.out.println("teamAgg.getName():"+teamAgg.getName());
		System.out.println("teamAgg.getType():"+teamAgg.getType());
		System.out.println("teamAgg.getValue():"+teamAgg.getValue());
		System.out.println("teamAgg.getWriteableName():"+teamAgg.getWriteableName());
		System.out.println("teamAgg.getMetaData():"+teamAgg.getMetaData());
		
		
		
		
		s=client.prepareSearch("pl*").addAggregation(AggregationBuilders.sum("sumMoney").field("money")).execute().actionGet();
		
		System.out.println(s.toString());
		aggMap=s.getAggregations().asMap();
		teamAgg=(InternalSum ) aggMap.get("sumMoney");  
		System.out.println(teamAgg.toString());
		System.out.println("teamAgg.getName():"+teamAgg.getName());
		System.out.println("teamAgg.getType():"+teamAgg.getType());
		System.out.println("teamAgg.getValue():"+teamAgg.getValue());
		System.out.println("teamAgg.getWriteableName():"+teamAgg.getWriteableName());
		System.out.println("teamAgg.getMetaData():"+teamAgg.getMetaData());
	} 
	
	/**
	 * 聚合函数求和-------------按多个条件聚合------------------------未实现
	 * @throws Exception
	 */
	public void getDataGroup2()throws Exception{
		SearchResponse s=client.prepareSearch("pl*").addAggregation(AggregationBuilders.sum("sumMoney").field("money"))
					.addAggregation(AggregationBuilders.sum("sumMoney").field("money"))
					.execute()
					.actionGet();
		
		System.out.println(s.toString());
		Map<String,Aggregation> aggMap=s.getAggregations().asMap();
		InternalSum teamAgg=(InternalSum ) aggMap.get("sumMoney");  
		System.out.println(teamAgg.toString());
		System.out.println("teamAgg.getName():"+teamAgg.getName());
		System.out.println("teamAgg.getType():"+teamAgg.getType());
		System.out.println("teamAgg.getValue():"+teamAgg.getValue());
		System.out.println("teamAgg.getWriteableName():"+teamAgg.getWriteableName());
		System.out.println("teamAgg.getMetaData():"+teamAgg.getMetaData());	
	}
	
	/**
	 * 聚合函数求平均值
	 * @throws Exception
	 */
	public void getDataAvg()throws Exception{
		AggregationBuilder agg=new AvgAggregationBuilder("avgMoney").field("money");
		SearchRequestBuilder group=client.prepareSearch("pl5").setTypes("user");
		
		group.addAggregation(agg);
		SearchResponse s=group.execute().actionGet();
		
		System.out.println(s.toString());
		Map<String,Aggregation>aggMap=s.getAggregations().asMap();
		InternalNumericMetricsAggregation.SingleValue  teamAgg=(InternalNumericMetricsAggregation.SingleValue ) aggMap.get("avgMoney");  
		System.out.println(teamAgg.toString());
		System.out.println("teamAgg.getName():"+teamAgg.getName());
		System.out.println("teamAgg.getType():"+teamAgg.getType());
		System.out.println("teamAgg.getValue():"+teamAgg.value());
		System.out.println("teamAgg.getWriteableName():"+teamAgg.getWriteableName());
		System.out.println("teamAgg.getMetaData():"+teamAgg.getMetaData());
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
	
	/**
	 * 删除数据
	 */
	public void deleteData(){
	    DeleteResponse response=client.prepareDelete("xue", "str", "2").get();
	    System.out.println("索引名称："+response.getIndex());
	    System.out.println("类型："+response.getType());
	    System.out.println("文档ID："+response.getId()); // 第一次使用是1
	    System.out.println("当前实例状态："+response.status());
	}
	
	/**
	 * 删除索引和数据
	 */
	public void deleteIndex(){
		 String indexName="pl5";  

		 
        //---------------删除方式--------如果没有找到索引，会抛异常 
        //可以根据DeleteIndexResponse对象的isAcknowledged()方法判断删除是否成功,返回值为boolean类型.  
        DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(indexName)  
                .execute().actionGet();  
        System.out.println("是否删除成功:"+dResponse.toString());  



        //---------------验证索引是否存在--------
        //如果传人的indexName不存在会出现异常.可以先判断索引是否存在：  
/*        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(indexName);  
          
        IndicesExistsResponse inExistsResponse = client.admin().indices()
                .exists(inExistsRequest).actionGet();  
        System.out.println("是否删除成功:"+inExistsResponse.isExists());  */
	}
	
	
	public static void main(String[] args) throws Exception {
		Elastic e=new Elastic();
//		e.addIndex4();
//		e.updateData();
//		e.deleteIndex();
//		e.getData();
		
		e.getDataGroup();
//		e.addIndex1();
		
	}

	public TransportClient getClient() {
		return client;
	}

	public void setClient(TransportClient client) {
		this.client = client;
	}
	
}
