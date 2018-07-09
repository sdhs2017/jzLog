package com.jz.bigdata.components.elasticsearch.spring.test.test2.test;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.util.List;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jz.bigdata.business.logAnalysis.log.entity.Log4j;
import com.jz.bigdata.business.logAnalysis.log.init.JzLogESinit;
import com.jz.bigdata.framework.spring.es.elasticsearch.ClientTemplate;
import com.jz.bigdata.framework.spring.es.example.EsTemplateTest;
import com.jz.bigdata.framework.spring.es.example.entity.Article;

import net.sf.json.JSONArray;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-mybatis.xml")
public class EsTest{
	
	@Autowired protected ClientTemplate clientTemplate;
	
	@Test
	public void test2(){
//		List<Map<String, Object>> list = clientTemplate.prepareSearch("estest", "Log4j", "AV6FBY7KXlvRoY9aku1N");
		String result = clientTemplate.searchById("estest", "Log4j", "AV6FBY7KXlvRoY9aku1N");
//		String result = JSONArray.fromObject(list).toString();
		System.out.println(result);
	}
	
	@Test
	public void initMapping(){
		String index = "estest";
		String type = "Log4j";
		new JzLogESinit().init(index, type, new Log4j(), clientTemplate);
	}
	
	@Test
	public void countGroupBy(){
		String index = "estest";
		String type = "Log4j";
		String param = "equipmentid";
		List<Map<String, Object>> list = new JzLogESinit().countGroupBy(index,type,param,clientTemplate);
		String result = JSONArray.fromObject(list).toString();
		System.err.println(result);
	}
	
	@Test
	public void deleteIndex(){
		String index = "estest";
		new JzLogESinit().deleteIndex(index, clientTemplate);
	}
	
	@Test
	public void test(){
		System.out.println(123);
		EsTemplateTest et = new EsTemplateTest();
		
		String indices = "article";
		String type = "doc";
		String id = "2001";
		
		Article a = new Article();
		a.setId(indices+"_"+type+"_"+id+"_"+"20170901"+"_"+id);
		a.setAuthor("author"+id);
		a.setTitle("title"+id);
		a.setContent("content"+id);
		a.setDescribe("describe"+id);
		
		et.insertOrUpdate(indices, type, id, generateJson(a),clientTemplate);
	}
	
	@Test
	public void deleteByIndex(){
		clientTemplate.deleteByIndex("estest");
	}
	
	public static XContentBuilder generateJson(Article article){
		XContentBuilder builder = null;
		String resultJson;
		try{
			builder = jsonBuilder()
				    .startObject()
				        .field("id", article.getId())
				        .field("title", article.getTitle())
				        .field("author", article.getAuthor())
				        .field("content", article.getContent())
				        .field("describe", article.getDescribe())
				    .endObject();
			resultJson = builder.string();
//			System.out.println(resultJson);
		}catch(Exception e){
			e.printStackTrace();
		}
		return builder;
	}

}
