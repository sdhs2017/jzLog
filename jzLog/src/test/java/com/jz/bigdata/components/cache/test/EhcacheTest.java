package com.jz.bigdata.components.cache.test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jz.bigdata.components.cache.ehcache.test.EhcacheTest3;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-mybatis.xml")
public class EhcacheTest {
	
	@Test
	public void test2(){
		EhcacheTest3 ec = new EhcacheTest3();
		Date start=new Date();   
		int y = ec.test();
		System.out.println(y);
		Date end=new Date(); 
		
		System.out.println((end.getTime()-start.getTime()));
		
		
		Date start2=new Date();   
		int y2 = ec.test();
		System.out.println(y2);
		Date end2=new Date(); 
		
		System.out.println((end2.getTime()-start2.getTime()));
	}

}
