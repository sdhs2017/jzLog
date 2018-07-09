package com.jz.bigdata.framework.spring.es.crud.entity;

import java.io.Serializable;
import java.util.Date;

import org.apache.htrace.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "article", type = "doc")
//@Setting(settingPath = "elasticsearch-analyser.json")
public class ArticleInf implements Serializable{
	
	//elasticsearch
    @Id
    private Integer articleInfId;

//    @Field(type = FieldType.String, analyzer="ngram_analyzer")//使用ngram进行单字分词
    @Field(type = FieldType.text)//使用ngram进行单字分词
    private String articleTitle;

    @Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    private Date releaseTime;

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getArticleTitle() {
        return articleTitle;
    }


    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

}
