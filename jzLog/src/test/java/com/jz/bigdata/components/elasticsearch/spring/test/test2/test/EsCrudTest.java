package com.jz.bigdata.components.elasticsearch.spring.test.test2.test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jz.bigdata.framework.spring.es.crud.entity.Article;
import com.jz.bigdata.framework.spring.es.crud.entity.ArticleInf;
import com.jz.bigdata.framework.spring.es.crud.entity.Author;
import com.jz.bigdata.framework.spring.es.crud.entity.Tutorial;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-mybatis.xml")
public class EsCrudTest {
	
//	@Autowired
//	private ArticleInfSearchService articleSearchRepository;
	
//	@Autowired
//	private ArticleInfSearchRepository articleInfSearchRepository;
	
//	@Autowired
//    ElasticsearchTemplate elasticsearchTemplate;
	
	@Test
	public void test(){
		String result = "123";
		ArticleInf a = new ArticleInf();
		a.setArticleTitle("article1");
		a.setReleaseTime(new Date());
//		elasticsearchTemplate.putMapping("article", "doc", a);
//		articleInfSearchRepository.save(a);
		
//		articleSearchRepository.save(a);
		System.out.println(result);
	}
//	@Test
//	public void test(){
//		ArticleInf a = new ArticleInf();
//		a.setArticleTitle("article1");
//		a.setReleaseTime(new Date());
////		elasticsearchTemplate.putMapping("article", "doc", a);
////		articleInfSearchRepository.save(a);
//		
//		boolean result = elasticsearchTemplate.createIndex("article");
//		System.out.println(result);
//	}
	
//	@Test
//	public void testSaveArticleIndex(){
//		Author author=new Author();
//		author.setId("1");
//		author.setName("tianshouzhi");
//		author.setRemark("java developer");
//		
//		Tutorial tutorial=new Tutorial();
//		tutorial.setId("1");
//		tutorial.setName("elastic search");
//		
//		Article article =new Article();
//		article.setId("1");
//		article.setTitle("springboot integreate elasticsearch");
//		article.setAbstracts("springboot integreate elasticsearch is very easy");
//		article.setTutorial(tutorial);
//		article.setAuthor(author);
//		article.setContent("elasticsearch based on lucene,"
//				+ "spring-data-elastichsearch based on elaticsearch"
//				+ ",this tutorial tell you how to integrete springboot with spring-data-elasticsearch");
//		article.setPostTime(new Date());
//		article.setClickCount(1L);
//		
//		articleSearchRepository.save(article);
//	}

}
