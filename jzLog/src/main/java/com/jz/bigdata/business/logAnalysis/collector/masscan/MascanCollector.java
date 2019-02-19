package com.jz.bigdata.business.logAnalysis.collector.masscan;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jz.bigdata.util.ExecuteCmd;

public class MascanCollector implements Runnable{

	private Semaphore semaphore;
	
	private String IPS;
	
	private String [] ports;
	
	
	final long awaitTime = 100 * 1000; 
	
	public MascanCollector() {}
	
	public MascanCollector(Semaphore semaphore,String IPS,String[] ports) {
		this.semaphore = semaphore;
		this.IPS = IPS;
		this.ports = ports;
	}
	
 	/**
 	 * 重写线程执行内容
 	 */
	@Override
	public void run() {
		
		try {
			Date starttime = new Date();
			
			// 获取 信号量 执行许可
			semaphore.acquire();
			File file = new File("/opt/jzlog/masscan/bin");
			String [] rgexs = {"\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}","Discovered"};
			Map<String, String> result = ExecuteCmd.execCmd("./masscan 192.168.0.8 -p10-1000", file, rgexs,true);
			System.out.println(result);
			// 释放 信号量 许可
			semaphore.release();
			Date endtime = new Date();
			
			System.out.println("开始时间"+starttime.getTime()+",结束时间"+endtime.getTime()+"    "+(endtime.getTime()-starttime.getTime()));
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void execute(ArrayList<String> list,String [] ports) {
		
		final Semaphore semaphore = new Semaphore(5);
		// 线程池（异步执行机制）
		ExecutorService threadPool = Executors.newCachedThreadPool();
		
		for(String ip : list) {
			threadPool.execute(new MascanCollector(semaphore,ip,ports));
		}
		//threadPool.shutdownNow();
		
		/* try {
			 
	        // (所有的任务都结束的时候，返回TRUE)  
	        if(!threadPool.awaitTermination(awaitTime, TimeUnit.MILLISECONDS)){  
	            // 超时的时候向线程池中所有的线程发出中断(interrupted)。  
	        	threadPool.shutdownNow();  
	        }  
	    } catch (InterruptedException e) {  
	        // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。  
	        System.out.println("awaitTermination interrupted: " + e);  
	        threadPool.shutdownNow();  
	    }*/
		
		if (threadPool.isTerminated()) {
			threadPool.shutdownNow();
		}
		
	}
	
	public static void main(String [] args) {
		MascanCollector mascanCollector = new MascanCollector();
		ArrayList<String> list = new ArrayList<String>();
		list.add("192.168.0.1");
		list.add("192.168.0.2");
		list.add("192.168.0.3");
		list.add("192.168.0.4");
		list.add("192.168.0.5");
		list.add("192.168.0.6");
		list.add("192.168.0.7");
		list.add("192.168.0.8");
		list.add("192.168.0.9");
		list.add("192.168.0.10");
		list.add("192.168.0.11");
		list.add("192.168.0.12");
		list.add("192.168.0.13");
		list.add("192.168.0.14");
		list.add("192.168.0.15");
		list.add("192.168.0.16");
		list.add("192.168.0.17");
		list.add("192.168.0.18");
		list.add("192.168.0.19");
		list.add("192.168.0.20");
		
		String [] ports = {"1","2"};
		
		
		mascanCollector.execute(list,ports);
		//mascanCollector.session.disconnect();
	}

}
