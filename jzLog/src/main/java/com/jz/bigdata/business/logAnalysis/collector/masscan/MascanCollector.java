package com.jz.bigdata.business.logAnalysis.collector.masscan;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import com.jz.bigdata.common.masscanip.entity.Masscanip;
import com.jz.bigdata.common.masscanip.service.IMasscanipService;
import com.jz.bigdata.util.ExecuteCmd;
import com.jz.bigdata.util.Uuid;

public class MascanCollector implements Runnable {

	private Semaphore semaphore;

	private String IPS;

	private String[] ports;

	
	public Semaphore getSemaphore() {
		return semaphore;
	}

	public void setSemaphore(Semaphore semaphore) {
		this.semaphore = semaphore;
	}

	public String getIPS() {
		return IPS;
	}

	public void setIPS(String iPS) {
		IPS = iPS;
	}

	public String[] getPorts() {
		return ports;
	}

	public void setPorts(String[] ports) {
		this.ports = ports;
	}

	final long awaitTime = 100 * 1000;
	private ExecutorService threadPool=Executors.newCachedThreadPool();
	private IMasscanipService masscanipService;
	
	public MascanCollector(Semaphore semaphore, String IPS, String[] ports,IMasscanipService masscanipService) {
		this.semaphore = semaphore;
		this.IPS = IPS;
		this.ports = ports;
		this.masscanipService=masscanipService;
	}
	


	/**
	 * 重写线程执行内容
	 */
	@Override
	public void run() {

		try {
//			Date starttime = new Date();

			// 获取 信号量 执行许可
			semaphore.acquire();
			File file = new File("/opt/jzlog/masscan/bin");
			String[] rgexs = { "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}", "Discovered" };
			Map<String, String> result = ExecuteCmd.execCmd("./masscan "+IPS+" -p10-1000", file, rgexs, true);
			System.out.println(result);
			// 释放 信号量 许可
			semaphore.release();
			Date endtime = new Date();
			String resultIp=result.get("./masscan "+IPS+" -p10-1000");
			resultIp=getSubUtil(resultIp,"\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String time = format.format(endtime.getTime());//这个就是把时间戳经过处理得到期望格式的时间
	        Masscanip masscanip =new Masscanip();
	        masscanip.setId(Uuid.getUUID());
	        masscanip.setIp(resultIp);
	        masscanip.setDate(time);
	        masscanipService.insert(masscanip);


		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public MascanCollector(List<String> list, String[] ports,IMasscanipService masscanipService) {

		final Semaphore semaphore = new Semaphore(5);

		for (String ip : list) {
			threadPool.execute(new MascanCollector(semaphore, ip, ports,masscanipService));
		}
		 threadPool.shutdown();

		/*
		 * try {
		 * 
		 * // (所有的任务都结束的时候，返回TRUE) if(!threadPool.awaitTermination(awaitTime,
		 * TimeUnit.MILLISECONDS)){ // 超时的时候向线程池中所有的线程发出中断(interrupted)。
		 * threadPool.shutdownNow(); } } catch (InterruptedException e) { //
		 * awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。 System.out.println(
		 * "awaitTermination interrupted: " + e); threadPool.shutdownNow(); }
		 */

		if (threadPool.isTerminated()) {
			threadPool.shutdownNow();
		}

	}

	public static void main(String[] args) {
		
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
		String[] ports = { "1", "2" };
//		MascanCollector mascanCollector = new MascanCollector(list,ports);
		

		

		// mascanCollector.execute(list,ports);
		// mascanCollector.session.disconnect();
	}

	/**
	 * @return
	 * 查看线程运行状态(ture：运行完成  false：线程未运行结束)
	 */
	public Boolean getStarted() {
		return threadPool.isTerminated();
	}
	
	/**
	 * 正则匹配
	 * @param soap
	 * @param rgex
	 * @return 返回匹配的内容
	 */
 	public static String getSubUtil(String soap,String rgex){  
         Pattern pattern = Pattern.compile(rgex);// 匹配的模式  
         Matcher m = pattern.matcher(soap);  
         while(m.find()){
             return m.group(0);
         }  
         return null;  
    }
}
