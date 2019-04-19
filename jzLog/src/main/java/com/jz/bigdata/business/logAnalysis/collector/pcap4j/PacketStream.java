package com.jz.bigdata.business.logAnalysis.collector.pcap4j;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.elasticsearch.action.index.IndexRequest;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;

import com.google.gson.Gson;
import com.jz.bigdata.business.logAnalysis.log.LogType;
import com.jz.bigdata.business.logAnalysis.log.entity.DefaultPacket;
import com.jz.bigdata.business.logAnalysis.log.entity.Http;
import com.jz.bigdata.framework.spring.es.elasticsearch.ClientTemplate;
import com.jz.bigdata.util.ConfigProperty;

public class PacketStream {

	
	final Logger logger = Logger.getLogger(PacketStream.class);
	
	private Http http;
	private DefaultPacket defaultpacket;
	private Set<String> url ;
	
	boolean iDestroy = false;
	
	ConfigProperty configProperty;
	ClientTemplate clientTemplate;
	Gson gson;
	String json;
	
	private ProtocolListener listener = new TLSProtocolListener();
	private HashMap<String,LinkedList<TcpPacket>> ackSendBuffer=new HashMap<String,LinkedList<TcpPacket>>();
	private HashMap<String,LinkedList<TcpPacket>> ackRecvBuffer=new HashMap<String,LinkedList<TcpPacket>>();
	
	List<IndexRequest> requests ;
	
	
	public PacketStream(ConfigProperty configProperty,ClientTemplate clientTemplate,Gson gson,List<IndexRequest> requests,Set<String> url)
	{
		this.configProperty = configProperty;
		this.clientTemplate = clientTemplate;
		this.gson = gson;
		this.requests = requests;
		this.url = url;
	}
	
	
	
	public void gotPacket(Packet packet){
		
		try {
			//TcpPacket tcppacket =packet.getBuilder().getPayloadBuilder().build().get(TcpPacket.class);
			IpV4Packet ip4packet =packet.get(IpV4Packet.class);
			
			// 识别http数据包的正则表达式
			String httpRequest = "^(POST|GET) /[^\\s]* HTTP/1.[0,1]";
			String httpResponse = "^HTTP/1.[0,1] [0-9]{0,3} *";
			
			if (ip4packet.getHeader().getProtocol().toString().contains("TCP")) {
				TcpPacket tcpPacket = packet.get(TcpPacket.class);
				String dst_port = tcpPacket.getHeader().getDstPort().valueAsString();
				if ((getSubUtil(hexStringToString(tcpPacket.toHexString()), httpRequest)!=""||getSubUtil(hexStringToString(tcpPacket.toHexString()), httpResponse)!="")&&!dst_port.equals("9300")) {
					http =new Http(packet);
					if (http.getRequest_url()!=null) {
						url.add("http://"+http.getDes_ip()+":"+http.getDes_port()+""+getSubUtil(http.getRequest_url(),"[/].*?[/]"));
						System.out.println(url);
					}
					json = gson.toJson(http);
					requests.add(clientTemplate.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_HTTP, json));
				}else {
					defaultpacket = new DefaultPacket(packet);
					if (defaultpacket.getApplication_layer_protocol()!=null&&defaultpacket.getApplication_layer_protocol().equals("HTTPS")) {
						defaultpacket.setEncryption_based_protection_protocol(GetEncryptionProtocol(packet));
					}
					// 172.16.0.233 过滤与192.168.2.182 9300端口的交互数据
					/*if (defaultpacket.getProtocol_name().equals("TCP")) {
						if ((defaultpacket.getIpv4_dst_addr().equals("192.168.2.182")&&defaultpacket.getL4_dst_port().equals("9300"))||(defaultpacket.getIpv4_src_addr().equals("192.168.2.182")&&defaultpacket.getL4_src_port().equals("9300"))) {
							System.out.println("该数据不入库");
							logger.info("-----------tcp协议 中过滤本机与192.168.2.182:9300的交互数据----------");
						}
					}else {
						json = gson.toJson(defaultpacket);
						requests.add(clientTemplate.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_DEFAULTPACKET, json));
					}*/
					json = gson.toJson(defaultpacket);
					requests.add(clientTemplate.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_DEFAULTPACKET, json));
				}
			}else {
				defaultpacket = new DefaultPacket(packet);
				if (defaultpacket.getApplication_layer_protocol()!=null&&defaultpacket.getApplication_layer_protocol().equals("HTTPS")) {
					defaultpacket.setEncryption_based_protection_protocol(GetEncryptionProtocol(packet));
				}
				/*if (defaultpacket.getProtocol_name().equals("TCP")) {
					if ((defaultpacket.getIpv4_dst_addr().equals("192.168.2.182")&&defaultpacket.getL4_dst_port().equals("9300"))||(defaultpacket.getIpv4_src_addr().equals("192.168.2.182")&&defaultpacket.getL4_src_port().equals("9300"))) {
						System.out.println("该数据不入库");
						logger.info("-----------tcp协议 中过滤本机与192.168.2.182:9300的交互数据----------");
					}
				}else {
					json = gson.toJson(defaultpacket);
					requests.add(clientTemplate.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_DEFAULTPACKET, json));
				}*/
				json = gson.toJson(defaultpacket);
				requests.add(clientTemplate.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_DEFAULTPACKET, json));
			}
			
		
			if (requests.size()==configProperty.getEs_bulk()) {
				try {
					clientTemplate.bulk(requests);
					requests.clear();
				} catch (Exception e) {
					logger.error("----------------jiyourui-----clientTemplate.bulk------报错信息：-----"+e.getMessage());
					e.printStackTrace();
				}
				
			}
		} catch (Exception e) {
			logger.error("----------------jiyourui-----gotPacket------报错信息：-----"+e.getMessage());
			e.printStackTrace();
		}
		



	
	
		
	}
	
	public boolean enDestroy()
	{
		return iDestroy;
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
         return "";  
    }
 	
 	/**
 	 * 16进制转换成为string类型字符串
 	 * @param s
 	 * @return
 	 */
 	public static String hexStringToString(String s) {
 	    if (s == null || s.equals("")) {
 	        return null;
 	    }
 	    s = s.replace(" ", "");
 	    byte[] baKeyword = new byte[s.length() / 2];
 	    for (int i = 0; i < baKeyword.length; i++) {
 	        try {
 	            baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
 	        } catch (Exception e) {
 	            e.printStackTrace();
 	        }
 	    }
 	    try {
 	        s = new String(baKeyword, "UTF-8");
 	        new String();
 	    } catch (Exception e1) {
 	        e1.printStackTrace();
 	    }
 	    return s;
 	}
 	
 	/**
 	 * @param packet
 	 * @return
 	 * 获取加密协议
 	 */
 	public  String GetEncryptionProtocol(Packet packet){
		TcpPacket tcppacket = packet.getBuilder().getPayloadBuilder().build().get(TcpPacket.class);
		IpV4Packet ip4packet = packet.get(IpV4Packet.class);
		//定义seq
		String ackSeq = "";
		String server = "";
		//获取ackseq
		ackSeq = Long.toString(tcppacket.getHeader().getAcknowledgmentNumberAsLong());

		boolean isSend = false;
		//是否存在serverIP
		if (ip4packet.getHeader().getDstAddr().toString().contains(server)) {
			isSend = true;
		} else {
			isSend = false;
		}
		//这个包是带数据的，发送端告诉接收端，
		//这个数据包以及以前接收到的数据包需要交给应用层立即进行处理
		String version="";
		if (tcppacket.getHeader().getPsh()) {
			//是否包含serviceIp
			if (!isSend) {
				LinkedList<TcpPacket> ps = ackSendBuffer.get(ackSeq);
				if (ps == null) {
					ps = new LinkedList<TcpPacket>();
					ackSendBuffer.put(ackSeq, ps);
				}
				ps.add(tcppacket);
				//获取count
				int sendCount = tcppacket.getHeader().getSequenceNumber()
						- tcppacket.getHeader().getSequenceNumber() + tcppacket.getRawData().length
						- tcppacket.getHeader().getRawData().length;
				byte[] buffer = new byte[sendCount];
				int pos = 0;
				//遍历packet
				for (TcpPacket p : ackSendBuffer.get(ackSeq)) {
					//获取长度
					int packdatalength = p.getRawData().length - p.getHeader().getRawData().length;
					//长度为0跳出循环
					if (packdatalength == 0)
						continue;
					//复制数组
					System.arraycopy(p.getRawData(), p.getHeader().getRawData().length, buffer, pos,
							packdatalength);
					pos += packdatalength;
				}
				//获取秘钥信息
				version =this.listener.onSend(ackSeq, buffer);
				buffer = null;
				//删除map
				ackSendBuffer.remove(ackSeq);
//				return version;
			} else {
				//舒适化赋值
				LinkedList<TcpPacket> ps = ackRecvBuffer.get(ackSeq);
				if (ps == null) {
					ps = new LinkedList<TcpPacket>();
					ackRecvBuffer.put(ackSeq, ps);
				}
				ps.add(tcppacket);
				//获取count
				int recvCount = tcppacket.getHeader().getSequenceNumber()
						- tcppacket.getHeader().getSequenceNumber() + tcppacket.getRawData().length
						- tcppacket.getHeader().getRawData().length;
				byte[] buffer = new byte[recvCount];
				int pos = 0;
				//遍历packet
				for (TcpPacket p : ackRecvBuffer.get(ackSeq)) {
					//获取数据长度
					int packdatalength = p.getRawData().length - p.getHeader().getRawData().length;
					if (packdatalength == 0)
						continue;
					//复制数组
					System.arraycopy(p.getRawData(), p.getHeader().getRawData().length, buffer, pos,
							packdatalength);
					pos += packdatalength;
				}
				//获取秘钥信息
				version=this.listener.onRecv(ackSeq, buffer);
				buffer = null;
				//删除map
				ackRecvBuffer.remove(ackSeq);
//				return version;
			}

		}
		if (tcppacket.getHeader().getFin()) {
			iDestroy = true;
		}
		return version;
	}
 	
 	public static void main(String [] args) {
	/*	String method = "50 4f 53 54 20 2f 6a 7a 4c 6f 67 2f 65 71 75 69 70 6d 65 6e 74 2f 73 65 6c 65 63 74 50 61 67 65 2e 64 6f 20 48 54 54 50 2f 31 2e 31 0d 0a 48 6f 73 74 3a 20 31 39 32 2e 31 36 38 2e 32 2e 31 38 32 3a 38 30 38 30 0d 0a 55 73 65 72 2d 41 67 65 6e 74 3a 20 4d 6f 7a 69 6c 6c 61 2f 35 2e 30 20 28 57 69 6e 64 6f 77 73 20 4e 54 20 31 30 2e 30 3b 20 57 69 6e 36 34 3b 20 78 36 34 3b 20 72 76 3a 36 36 2e 30 29 20 47 65 63 6b 6f 2f 32 30 31 30 30 31 30 31 20 46 69 72 65 66 6f 78 2f 36 36 2e 30 0d 0a 41 63 63 65 70 74 3a 20 61 70 70 6c 69 63 61 74 69 6f 6e 2f 6a 73 6f 6e 2c 20 74 65 78 74 2f 6a 61 76 61 73 63 72 69 70 74 2c 20 2a 2f 2a 3b 20 71 3d 30 2e 30 31 0d 0a 41 63 63 65 70 74 2d 4c 61 6e 67 75 61 67 65 3a 20 7a 68 2d 43 4e 2c 7a 68 3b 71 3d 30 2e 38 2c 7a 68 2d 54 57 3b 71 3d 30 2e 37 2c 7a 68 2d 48 4b 3b 71 3d 30 2e 35 2c 65 6e 2d 55 53 3b 71 3d 30 2e 33 2c 65 6e 3b 71 3d 30 2e 32 0d 0a 41 63 63 65 70 74 2d 45 6e 63 6f 64 69 6e 67 3a 20 67 7a 69 70 2c 20 64 65 66 6c 61 74 65 0d 0a 52 65 66 65 72 65 72 3a 20 68 74 74 70 3a 2f 2f 31 39 32 2e 31 36 38 2e 32 2e 31 38 32 3a 38 30 38 30 2f 6a 7a 4c 6f 67 2f 76 69 65 77 2f 64 65 76 69 63 65 2f 64 65 76 69 63 65 2e 68 74 6d 6c 0d 0a 43 6f 6e 74 65 6e 74 2d 54 79 70 65 3a 20 61 70 70 6c 69 63 61 74 69 6f 6e 2f 78 2d 77 77 77 2d 66 6f 72 6d 2d 75 72 6c 65 6e 63 6f 64 65 64 3b 20 63 68 61 72 73 65 74 3d 55 54 46 2d 38 0d 0a 58 2d 52 65 71 75 65 73 74 65 64 2d 57 69 74 68 3a 20 58 4d 4c 48 74 74 70 52 65 71 75 65 73 74 0d 0a 43 6f 6e 74 65 6e 74 2d 4c 65 6e 67 74 68 3a 20 35 32 0d 0a 43 6f 6e 6e 65 63 74 69 6f 6e 3a 20 6b 65 65 70 2d 61 6c 69 76 65 0d 0a 43 6f 6f 6b 69 65 3a 20 4a 53 45 53 53 49 4f 4e 49 44 3d 39 38 38 39 43 36 32 38 43 32 46 41 37 41 42 42 30 39 30 30 33 41 36 35 31 45 38 38 39 42 39 45 0d 0a 0d 0a 6e 61 6d 65 3d 26 68 6f 73 74 4e 61 6d 65 3d 26 69 70 3d 26 6c 6f 67 54 79 70 65 3d 26 70 61 67 65 49 6e 64 65 78 3d 31 26 70 61 67 65 53 69 7a 65 3d 31 35";
		System.out.println(method.replace(" ", "").length());
		System.out.println(hexStringToString(method));*/
 		
 		/*String log = "Expert Info (Chat/Sequence): HTTP/1.1 200 OK\r\n";
 		
 		System.out.println(TcpStream.getSubUtil(log, "HTTP/1.[0,1] [0-9]{0,3} *"));*/
	}
}
