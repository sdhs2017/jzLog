package com.jz.bigdata.framework.spring.es.example.entity;

import java.io.Serializable;

import org.springframework.data.elasticsearch.annotations.Document;

/**
 * 文章 实体类
 * @author Dy
 *
 */
@Document(indexName="article",type="doc",indexStoreType="fs",shards=5,replicas=1,refreshInterval="-1")
public class Article implements Serializable{
	
	private String id;	//
	private String title;	//
	private String author;	//
	private String describe;	//
	private String content;	//
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

}
