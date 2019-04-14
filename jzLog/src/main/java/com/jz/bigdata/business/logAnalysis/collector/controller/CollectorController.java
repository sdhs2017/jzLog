package com.jz.bigdata.business.logAnalysis.collector.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapAddress;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapIpV4Address;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.core.BpfProgram.BpfCompileMode;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jz.bigdata.business.logAnalysis.collector.pcap4j.Pcap4jCollector;
import com.jz.bigdata.business.logAnalysis.collector.pcap4j.TcpStream;
import com.jz.bigdata.business.logAnalysis.collector.service.ICollectorService;
import com.jz.bigdata.common.alarm.service.IAlarmService;
import com.jz.bigdata.common.assets.service.IAssetsService;
import com.jz.bigdata.common.equipment.service.IEquipmentService;
import com.jz.bigdata.common.users.service.IUsersService;
import com.jz.bigdata.framework.spring.es.elasticsearch.ClientTemplate;
import com.jz.bigdata.util.ConfigProperty;
import com.jz.bigdata.util.DescribeLog;

import net.sf.json.JSONArray;

@Controller
@RequestMapping("/collector")
public class CollectorController {

	@Resource(name = "CollectorService")
	private ICollectorService collectorService;

	@Resource(name = "EquipmentService")
	private IEquipmentService equipmentService;

	@Resource(name = "configProperty")
	private ConfigProperty configProperty;

	@Resource(name = "AlarmService")
	private IAlarmService alarmService;

	@Resource(name = "UsersService")
	private IUsersService usersService;

	@Autowired
	protected ClientTemplate clientTemplate;

//	@Resource
//	private MascanCollector mascanCollector;
	@Resource(name="assetsService")
	private IAssetsService masscanipService;
	
	
	String client ="";
	
	Thread pcap4jthread = null;
	FutureTask<String> futureTask = null;
	Pcap4jCollector td  = null;

	// 获取采集器开启或关闭状态，true为开启，false为关闭
	@ResponseBody
	@RequestMapping("/getCollectorState")
	public boolean getCollectorState() {
		return collectorService.getKafkaCollectorState();
	}

	// 开启采集器
	@ResponseBody
	@RequestMapping(value = "/startCollectorState", produces = "application/json; charset=utf-8")
	@DescribeLog(describe = "开启数据采集器")
	public String startKafkaCollector() {
		boolean result = collectorService.startKafkaCollector(equipmentService, clientTemplate, configProperty,
				alarmService, usersService);
		Map<String, Object> map = new HashMap<>();
		if (result) {
			map.put("state", result);
			map.put("msg", "数据采集器开启成功");
			return JSONArray.fromObject(map).toString();
		} else {
			map.put("state", result);
			map.put("msg", "数据采集器开启失败，请勿重复开启");
			return JSONArray.fromObject(map).toString();
		}
	}

	// 关闭采集器
	@ResponseBody
	@RequestMapping(value = "/stopKafkaCollector", produces = "application/json; charset=utf-8")
	@DescribeLog(describe = "关闭数据采集器")
	public String stopKafkaCollector() {
		Map<String, Object> map = new HashMap<>();
		try {
			boolean result = collectorService.stopKafkaCollector();
			if (result) {
				map.put("state", result);
				map.put("msg", "数据采集器关闭成功");
				return JSONArray.fromObject(map).toString();
			} else {
				map.put("state", result);
				map.put("msg", "数据采集器关闭失败，已关闭");
				return JSONArray.fromObject(map).toString();
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
			map.put("state", false);
			map.put("msg", "数据采集器关闭失败");
			return JSONArray.fromObject(map).toString();
		}
	}

	// 监听采集器状态
	@ResponseBody
	@RequestMapping(value = "/stateKafkaCollector", produces = "application/json; charset=utf-8")
	@DescribeLog(describe = "监控数据采集器状态")
	public String stateKafkaCollector() {
		Map<String, Object> map = new HashMap<>();
		boolean result = collectorService.stateKafkaCollector();
		map.put("state", result);
		return JSONArray.fromObject(map).toString();
	}

	// 开启masscan扫描
	@ResponseBody
	@RequestMapping(value = "/startMasscanCollector", produces = "application/json; charset=utf-8")
	@DescribeLog(describe = "开启masscan扫描")
	public String startMasscanCollector(HttpServletRequest request) {
		
		String  ports = configProperty.getMasscan_ports();
		String startip=request.getParameter("startip");
		String endip=request.getParameter("endip");
		boolean resultstate = collectorService.stateMasscanCollector();
		Map<String, Object> map = new HashMap<>();
		if(resultstate==false){
			map.put("state", resultstate);
			map.put("msg", "资产扫描器开启失败，请勿重复开启");
			return JSONArray.fromObject(map).toString();
		}else{
			boolean result = collectorService.startMasscanCollector(startip,endip, ports,masscanipService,configProperty);
			if(result==true){
				map.put("state", result);
				map.put("msg", "资产扫描器开启成功");
				return JSONArray.fromObject(map).toString();
			}else{
				map.put("state", result);
				map.put("msg", "资产扫描器开启失败");
				return JSONArray.fromObject(map).toString();
			}
		}
		
	}
	
	// 监控Masscan状态
	@ResponseBody
	@RequestMapping(value = "/stateMasscanCollector", produces = "application/json; charset=utf-8")
	@DescribeLog(describe = "监控Masscan状态")
	public String stateMasscanCollector() {
		Map<String, Object> map = new HashMap<>();
		boolean result = collectorService.stateMasscanCollector();
		map.put("state", result);
		return JSONArray.fromObject(map).toString();
	}
	
	// pcap4j抓取数据包
	@ResponseBody
	@RequestMapping(value = "/startPcap4jCollector", produces = "application/json; charset=utf-8")
	@DescribeLog(describe = "开启pcap4j抓取数据包")
	/*public String startPcap4jCollector(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		
		try {
			pca = new Pcap4jCollectorTmp("192.168.0.103","true");
			
			map.put("state", pcap4jthread.isAlive());
			map.put("msg", "数据包采集器开启成功");
			return JSONArray.fromObject(map).toString();
		} catch (Exception e) {
			e.printStackTrace();
			map.put("state", false);
			map.put("msg", "数据包采集器开启失败");
			return JSONArray.fromObject(map).toString();
		}
		
	}*/
	public String startPcap4jCollector(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		
		
		HashMap<String, TcpStream> tcpStreamList=new HashMap<String, TcpStream>();
		PcapNetworkInterface nif = getCaptureNetworkInterface("192.168.0.103");
		
		if(nif==null)
        {
        	map.put("state", false);
			map.put("msg", "网卡获取失败！数据包采集器开启失败！");
			return JSONArray.fromObject(map).toString();
        }
		
    	for(PcapAddress a:nif.getAddresses())
        {
        	if(a instanceof PcapIpV4Address)
        	{
        		client = a.getAddress().toString();
        		break;
        	}
        }
        if(client.equals(""))
        {
        	map.put("state", false);
			map.put("msg", "网卡获取失败！数据包采集器开启失败！");
			return JSONArray.fromObject(map).toString();
        }
        // 抓取包长度
        int snaplen = 64 * 1024;
        // 超时50ms
        int timeout = 50;
        // 初始化抓包器
        PcapHandle.Builder phb = new PcapHandle.Builder(nif.getName()).snaplen(snaplen)
            .promiscuousMode(PromiscuousMode.PROMISCUOUS).timeoutMillis(timeout)
            .bufferSize(1 * 1024 * 1024);
        PcapHandle handle = null;
		try {
			handle = phb.build();
		} catch (PcapNativeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // handle = nif.openLive(snaplen, PromiscuousMode.NONPROMISCUOUS, timeout);

        /** 设置TCP过滤规则 */
        //String filter = "ip and tcp and (port 443)";
        /** 设置TCP过滤规则 */
        String filter = "ip and tcp and (port 443)";
        
            
        // 设置过滤器
        try {
			handle.setFilter(filter, BpfCompileMode.OPTIMIZE);
		} catch (PcapNativeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotOpenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //初始化listener
        PacketListener listener = new PacketListener() {
        	
        	 public void gotPacket(Packet packet) {
        		 // public void gotPacket(PcapPacket packet) {
            	
            	
            	TcpPacket tcppacket =packet.getBuilder().getPayloadBuilder().build().get(TcpPacket.class);
    			IpV4Packet ip4packet =packet.get(IpV4Packet.class);
    			
    			String server = "";
    			String serverPort ="";
    			if(ip4packet.getHeader().getDstAddr().toString().contains(client))
    			{
    				server=ip4packet.getHeader().getSrcAddr().toString();
    				serverPort = tcppacket.getHeader().getSrcPort().valueAsString();
    				
    			}else if(ip4packet.getHeader().getSrcAddr().toString().contains(client))
    			{
    				server=ip4packet.getHeader().getDstAddr().toString();
    				serverPort = tcppacket.getHeader().getDstPort().valueAsString();
    			}else
    			{
    				return;
    			}
    			
    			TcpStream tps =null;
            	if(tcpStreamList.get(server+serverPort) == null)
            	{
            		tps= new TcpStream(server,client);
            		tcpStreamList.put(server+serverPort, tps);
            		tps.gotPacket(packet);

            	}else
            	{
            		tps = tcpStreamList.get(server+serverPort);
            		tps.gotPacket(packet);
            	}
            	
            	if(tps.enDestroy())
            	{
            		tps = null;
            		tcpStreamList.put(server+serverPort, tps);
            		tcpStreamList.remove(server+serverPort);
            		
            	}
            }
		
        };
		
		try {
			td = new Pcap4jCollector("192.168.200.158",handle,listener);
			futureTask = new FutureTask<>(td);
			
			pcap4jthread = new Thread(futureTask);
			pcap4jthread.start();
			
			if(pcap4jthread.isAlive()==true){
				map.put("state", pcap4jthread.isAlive());
				map.put("msg", "数据包采集器开启成功");
				return JSONArray.fromObject(map).toString();
			}else{
				map.put("state", pcap4jthread.isAlive());
				map.put("msg", "数据包采集器开启失败");
				return JSONArray.fromObject(map).toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("state", false);
			map.put("msg", "数据包采集器开启失败");
			return JSONArray.fromObject(map).toString();
		}
		
	}
	
	
	// 监控pcap4j抓取数据包运行状态
	@ResponseBody
	@RequestMapping(value = "/statePcap4jCollector", produces = "application/json; charset=utf-8")
	@DescribeLog(describe = "监控pcap4j抓取数据包运行状态")
	public String statePcap4jCollector(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		if (td!=null) {
			map.put("state", td.getPcap4jStatus());
		}else {
			map.put("state", false);
		}
		
		return JSONArray.fromObject(map).toString();
	}
	
	// 停止pcap4j抓取数据包
	@SuppressWarnings("deprecation")
	@ResponseBody
	@RequestMapping(value = "/stopPcap4jCollector", produces = "application/json; charset=utf-8")
	@DescribeLog(describe = "停止pcap4j抓取数据包")
	public String stopPcap4jCollector(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		if (td!=null) {
			if (td.getPcap4jStatus()) {
				td.closePcap4j();
				map.put("state", true);
				map.put("msg", "数据包采集器关闭成功");
			}else {
				map.put("state", true);
				map.put("msg", "数据包采集器已经关闭");
			}
		}else {
			map.put("state", false);
			map.put("msg", "数据包采集器未启动");
		}
		
		return JSONArray.fromObject(map).toString();
	}
		
	/**
     * 根据IP获取指定网卡设备
	* @param localHost 网卡IP
	* 
	* @return 指定的设备对象
	*/
	public static PcapNetworkInterface getCaptureNetworkInterface(String localHost) {
		List<PcapNetworkInterface> allDevs;
		try {
			// 获取全部的网卡设备列表，Windows如果获取不到网卡信息，输入：net start npf  启动网卡服务
			allDevs = Pcaps.findAllDevs();
		
			 for (PcapNetworkInterface networkInterface : allDevs) {
			     List<PcapAddress> addresses = networkInterface.getAddresses();
			     for (PcapAddress pcapAddress : addresses) {
			         // 获取网卡IP地址
			         String ip = pcapAddress.getAddress().getHostAddress();
			//         System.out.println(ip);
			         if (ip != null && ip.contains(localHost)) {
			             // 返回指定的设备对象
			//         	System.out.println("filter:"+ip);
			         	
			             return networkInterface;
			         }
			
			     }
			 }
		} catch (PcapNativeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return null;
	}

}
