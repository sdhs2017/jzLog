package com.jz.bigdata.business.logAnalysis.log.entity;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;


/**
 * @ClassName Http
 * @Description 
 * @Author shi cheng yu
 * @Date 2019年4月12日 下午1:33:07
 */
public class Http {
	
	private String source_port;
	private String des_port;
	private String source_ip;
	private String des_ip;
	private String protocol;
	private String requestorresponse;
	private String request_url;
	private String response_state;
	private String request_type;
	/**
	 * id
	 */
	private String id;
	/**
	 * userid
	 *//*
	private String userid;
	*//**
	 * deptid
	 *//*
	private String deptid;
	*//**
	 * equipmentid
	 *//*
	private String equipmentid;
	*//**
	 * equipmentname
	 *//*
	private String equipmentname;
	*//**
	 * 日志时间
	 *//*
	private Date logdate;
	private String logtime;
	private String logtime_minute;
	private String logtime_hour;
	private String logtime_day;
	private String logtime_month;
	private String logtime_year;
*/
	/**
	 * 操作描述
	 */
	private String operation_des;

	public String getSource_port() {
		return source_port;
	}

	public void setSource_port(String source_port) {
		this.source_port = source_port;
	}

	public String getDes_port() {
		return des_port;
	}

	public void setDes_port(String des_port) {
		this.des_port = des_port;
	}

	public String getSource_ip() {
		return source_ip;
	}

	public void setSource_ip(String source_ip) {
		this.source_ip = source_ip;
	}

	public String getDes_ip() {
		return des_ip;
	}

	public void setDes_ip(String des_ip) {
		this.des_ip = des_ip;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getRequestorresponse() {
		return requestorresponse;
	}

	public void setRequestorresponse(String requestorresponse) {
		this.requestorresponse = requestorresponse;
	}
	
	public String getRequest_url() {
		return request_url;
	}

	public void setRequest_url(String request_url) {
		this.request_url = request_url;
	}

	public String getResponse_state() {
		return response_state;
	}

	public void setResponse_state(String response_state) {
		this.response_state = response_state;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/*public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public String getEquipmentid() {
		return equipmentid;
	}

	public void setEquipmentid(String equipmentid) {
		this.equipmentid = equipmentid;
	}

	public String getEquipmentname() {
		return equipmentname;
	}

	public void setEquipmentname(String equipmentname) {
		this.equipmentname = equipmentname;
	}

	public Date getLogdate() {
		return logdate;
	}

	public void setLogdate(Date logdate) {
		this.logdate = logdate;
	}

	public String getLogtime() {
		return logtime;
	}

	public void setLogtime(String logtime) {
		this.logtime = logtime;
	}

	public String getLogtime_minute() {
		return logtime_minute;
	}

	public void setLogtime_minute(String logtime_minute) {
		this.logtime_minute = logtime_minute;
	}

	public String getLogtime_hour() {
		return logtime_hour;
	}

	public void setLogtime_hour(String logtime_hour) {
		this.logtime_hour = logtime_hour;
	}

	public String getLogtime_day() {
		return logtime_day;
	}

	public void setLogtime_day(String logtime_day) {
		this.logtime_day = logtime_day;
	}

	public String getLogtime_month() {
		return logtime_month;
	}

	public void setLogtime_month(String logtime_month) {
		this.logtime_month = logtime_month;
	}

	public String getLogtime_year() {
		return logtime_year;
	}

	public void setLogtime_year(String logtime_year) {
		this.logtime_year = logtime_year;
	}*/

	public String getOperation_des() {
		return operation_des;
	}

	public void setOperation_des(String operation_des) {
		this.operation_des = operation_des;
	}

	

	public String getRequest_type() {
		return request_type;
	}

	public void setRequest_type(String request_type) {
		this.request_type = request_type;
	}

	/**
	 * @param packet
	 * 构造方法，填充数据
	 */
	public Http(Packet packet){
		IpV4Packet ip4packet =packet.get(IpV4Packet.class);
		TcpPacket tcpPacket = packet.get(TcpPacket.class);
//		System.out.println(log);
		
//		Pattern source_portPattern = Pattern.compile("Source port:");  
//        Matcher source_portmatcher = source_portPattern.matcher(log);
		this.source_ip=ip4packet.getHeader().getSrcAddr().toString();
		this.source_port=tcpPacket.getHeader().getSrcPort().valueAsInt()+"";
		this.des_ip=ip4packet.getHeader().getDstAddr().toString();
		this.des_port=tcpPacket.getHeader().getDstPort().valueAsInt()+"";
		this.protocol="http";
		String httphex = tcpPacket.getPayload().toString().substring(tcpPacket.getPayload().toString().indexOf(":")+1).trim();
		this.operation_des=hexStringToString(httphex);
		//httprequest
		String httpRequest = "[a-zA-Z]{3,7} .* HTTP/1.[0,1]";
		//httpResponse
		String httpResponse = "HTTP/1.[0,1] [0-9]{0,3} *";
		
		//获取数据是否为空
		if (httphex!=null&&!httphex.equals("")) {
			
			// http请求报文解析
			if (getSubUtil(hexStringToString(tcpPacket.toHexString()), httpRequest)!="") {
				this.requestorresponse="request";
//				this.operation_des=hexStringToString(httphex);
				String [] httpcontent = hexStringToString(httphex).split("\r\n");
				//根据空格分割数据
				String[] message=httpcontent[0].split("\\s+");
				//循环添加数据
				for(int i=0;i<message.length;i++){
					if(i==0){
						this.request_type=message[0];
					}else if(i==1){
						this.request_url=message[1];
					}
				}
			
			// http返回报文解析
			}else if (getSubUtil(hexStringToString(tcpPacket.toHexString()), httpResponse)!="") {
				this.requestorresponse="response";
				String [] httpcontent = hexStringToString(httphex).split("\r\n");
				//根据空格分割数据
				String[] message=httpcontent[0].split("\\s+");
				this.response_state=message[1];
			}
			
		}
		
        
        
	}
		
	public Http() {
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
		
	
	
	public String toMapping() {
		String template = "{\n" + "\t\t\"properties\":{\n" + "\t\t{#}\n" + "\t\t\t\t}" + "}";
		String fieldString = getClassMapping(new Http());
		template = template.replace("{#}", fieldString);
		return template;
	}

	public <T> String getClassMapping(T classes) {

		StringBuilder fieldstring = new StringBuilder();

		Field[] fields = classes.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			fieldstring.append("\t\t\t\t\"" + fields[i].getName().toLowerCase() + "\": {\n");
			fieldstring.append("\t\t\t\t\t\t\"type\": \""
					+ GetElasticSearchMappingType(fields[i].getType().getSimpleName(), fields[i].getName()) + "\n");
			if (fields[i].getName().equals("id")) {
				fieldstring.append("\t\t\t\t\t\t,\"index\": \"" + "false\"" + "\n");
			}
			if (fields[i].getName().equals("userid") || fields[i].getName().equals("deptid")
					|| fields[i].getName().equals("equipmentid") || fields[i].getName().equals("equipmentname")
					|| fields[i].getName().equals("source_port")|| fields[i].getName().equals("protocol")
					|| fields[i].getName().equals("des_port")|| fields[i].getName().equals("source_ip")
					|| fields[i].getName().equals("des_ip")|| fields[i].getName().equals("request_type")
					|| fields[i].getName().equals("request_url")|| fields[i].getName().equals("response_state")) {
				fieldstring.append("\t\t\t\t\t\t,\"fielddata\": " + "true" + "\n");
			}
			if (fields[i].getName().equals("operation_des") || fields[i].getName().equals("equipmentname")
					|| fields[i].getName().equals("des_port") || fields[i].getName().equals("source_port")
					|| fields[i].getName().equals("source_ip")
					|| fields[i].getName().equals("des_ip")|| fields[i].getName().equals("request_url")
					) {
				fieldstring.append("\t\t\t\t\t\t,\"analyzer\": \"" + "index_ansj\"" + "\n");
				fieldstring.append("\t\t\t\t\t\t,\"search_analyzer\": \"" + "query_ansj\"" + "\n");
			}
			if (i == fields.length - 1) {
				fieldstring.append("\t\t\t\t\t}\n");
			} else {
				fieldstring.append("\t\t\t\t\t},\n");
			}
		}
		return fieldstring.toString();
	}

	private static String GetElasticSearchMappingType(String varType, String name) {
		String es = "text";
		switch (varType) {
		case "Date":
			es = "date\"\n" + "\t\t\t\t\t\t,\"format\":\"yyyy-MM-dd HH:mm:ss\"\n" + "\t\t\t\t\t\t";
			break;
		case "Double":
			es = "double\"\n" + "\t\t\t\t\t\t,\"null_value\":\"NaN\"";
			break;
		case "Long":
			es = "long\"";
			break;
		default:
			if (name.equals("id")) {
				es = "keyword\"";
			} else {
				es = "text\"";
			}

			break;
		}
		return es;
	}
	
	public static void main(String[] args) {
		Http http =new Http();
		
		System.out.println(http.toMapping());
		
	}
	
	
}
