package com.jz.bigdata.framework.spring.es.example;


import java.util.Arrays;
import java.util.Comparator;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;

import com.jz.bigdata.framework.spring.es.elasticsearch.ClientCallback;
import com.jz.bigdata.framework.spring.es.elasticsearch.ClientTemplate;
import com.jz.bigdata.framework.spring.es.elasticsearch.ClusterCallback;


public class EsTemplateTest {
	
//	@Autowired protected ClientTemplate clientTemplate;
	
	/**
	 * 插入或修改文档
	 */
//	public IndexResponse insertOrUpdate(final String indices, final IndexType indexType, final String keyId, final Map<String, Object> source) {
//	    return clientTemplate.executeGet((Client client)->{
//	        IndexRequest request = Requests.indexRequest(indices).type(indexType.name()).id(keyId).source(source);
////	        logger.debug("insertOrUpdate-" + request.toString());
//	        ActionFuture<IndexResponse> res = client.index(request);
//	        clientTemplate.refreshIndex();
//	        return res;
//	    });
//	}
	
	public IndexResponse insertOrUpdate(final String indices, final String type, final String keyId, final XContentBuilder source,ClientTemplate clientTemplate) {
	    return clientTemplate.executeGet((Client client)->{
	        IndexRequest request = Requests.indexRequest(indices).type(type).id(keyId).source(source);
//	        logger.debug("insertOrUpdate-" + request.toString());
	        ActionFuture<IndexResponse> res = client.index(request);
	        clientTemplate.refreshIndex();
	        
	        return res;
	    });
	}
	
	public IndexResponse insertOrUpdate2(final String indices, final String type, final String keyId, final XContentBuilder source,ClientTemplate clientTemplate) {
	    
	    IndexResponse result = clientTemplate.executeGet((Client client)->{
	        IndexRequest request = Requests.indexRequest(indices).type(type).id(keyId).source(source);
	        ActionFuture<IndexResponse> res = client.index(request);
	        clientTemplate.refreshIndex();
	        
	        return res;
	    });
	    return result;
	}
	
	public IndexResponse insertOrUpdate3(final String indices, final String type, final String keyId, final XContentBuilder source,ClientTemplate clientTemplate) {
		
		ClientCallback<IndexResponse> callback = (Client client)->{
	        IndexRequest request = Requests.indexRequest(indices).type(type).id(keyId).source(source);
	        ActionFuture<IndexResponse> res = client.index(request);
	        clientTemplate.refreshIndex();
	        
	        return res;
	    };
	    IndexResponse result = clientTemplate.executeGet(callback);
	    return result;
	}
	
	public IndexResponse insertOrUpdate4(final String indices, final String type, final String keyId, final XContentBuilder source,ClientTemplate clientTemplate) {
		
		ClientCallback<IndexResponse> callback = new ClientCallback<IndexResponse>(){
			@Override
			public ActionFuture<IndexResponse> execute(Client client) {
		        IndexRequest request = Requests.indexRequest(indices).type(type).id(keyId).source(source);
		        ActionFuture<IndexResponse> res = client.index(request);
		        clientTemplate.refreshIndex();
		        return res;
			}
		};
	    IndexResponse result = clientTemplate.executeGet(callback);
	    return result;
	}	
	
	public static void main(String[] args){
		String[] players = {"Rafael Nadal", "Novak Djokovic",   
			    "Stanislas Wawrinka", "David Ferrer",  
			    "Roger Federer", "Andy Murray",  
			    "Tomas Berdych", "Juan Martin Del Potro",  
			    "Richard Gasquet", "John Isner"};  
		
		Comparator<String> sortByName2 = (String s1, String s2) -> (s1.compareTo(s2)); 
		
		Comparator<String> sortByName1 = new Comparator<String>() {
		    @Override  
		    public int compare(String s1, String s2) {
		        return (s1.compareTo(s2));  
		    }  
		};
		
		Comparator<String> sortByName = (String s1, String s2) -> (s1.compareTo(s2));  
		Arrays.sort(players, sortByName);  
		
		// 1.1 使用匿名内部类根据 name 排序 players  
		Arrays.sort(players, new Comparator<String>() {  
		    @Override  
		    public int compare(String s1, String s2) {  
		        return (s1.compareTo(s2));  
		    }  
		}); 
		

	}
	
 

	
	
	

}
