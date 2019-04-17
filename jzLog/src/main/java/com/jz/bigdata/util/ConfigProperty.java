package com.jz.bigdata.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component("configProperty")
@Configuration
public class ConfigProperty {

	@Value("#{prop.host_user}")
	private String host_user;
	
	@Value("#{prop.host_passwd}")
	private String host_passwd;  
    
	@Value("#{prop.host_ip}")
	private String host_ip;  
    
    @Value("#{prop.zookeeper_ip}")
    private String zookeeper_ip;
    
    @Value("#{prop.zookeeper_port}")
    private String zookeeper_port;
    
    @Value("#{prop.zookeeper_path}")
    private String zookeeper_path;
    
    @Value("#{prop.es_ip}")
    private String es_ip;
    
    @Value("#{prop.es_port}")
    private String es_port;
    
    @Value("#{prop.es_path}")
    private String es_path;
    
    @Value("#{prop.es_path_snapshot}")
    private String es_path_snapshot;
    
    @Value("#{prop.es_index}")
    private String es_index;
    
    @Value("#{prop.es_bulk}")
    private Integer es_bulk;
    
    @Value("#{prop.email_address}")
    private String email_address;
    
    @Value("#{prop.number}")
    private String number;
    
    @Value("#{prop.masscan_path}")
    private String masscan_path;
    
    @Value("#{prop.masscan_ports}")
    private String masscan_ports;
    
    @Value("#{prop.protocol}")
    private String protocol;
    
    @Value("#{prop.pcap4j_network}")
    private String pcap4j_network;

	public String getHost_user() {
		return host_user;
	}

	public void setHost_user(String host_user) {
		this.host_user = host_user;
	}

	public String getHost_passwd() {
		return host_passwd;
	}

	public void setHost_passwd(String host_passwd) {
		this.host_passwd = host_passwd;
	}

	public String getHost_ip() {
		return host_ip;
	}

	public void setHost_ip(String host_ip) {
		this.host_ip = host_ip;
	}

	public String getZookeeper_ip() {
		return zookeeper_ip;
	}

	public void setZookeeper_ip(String zookeeper_ip) {
		this.zookeeper_ip = zookeeper_ip;
	}

	public String getZookeeper_port() {
		return zookeeper_port;
	}

	public void setZookeeper_port(String zookeeper_port) {
		this.zookeeper_port = zookeeper_port;
	}

	public String getZookeeper_path() {
		return zookeeper_path;
	}

	public void setZookeeper_path(String zookeeper_path) {
		this.zookeeper_path = zookeeper_path;
	}

	public String getEs_ip() {
		return es_ip;
	}

	public void setEs_ip(String es_ip) {
		this.es_ip = es_ip;
	}

	public String getEs_port() {
		return es_port;
	}

	public void setEs_port(String es_port) {
		this.es_port = es_port;
	}

	public String getEs_path() {
		return es_path;
	}

	public void setEs_path(String es_path) {
		this.es_path = es_path;
	}

	public String getEs_path_snapshot() {
		return es_path_snapshot;
	}

	public void setEs_path_snapshot(String es_path_snapshot) {
		this.es_path_snapshot = es_path_snapshot;
	}

	public String getEs_index() {
		return es_index;
	}

	public void setEs_index(String es_index) {
		this.es_index = es_index;
	}

	public Integer getEs_bulk() {
		return es_bulk;
	}

	public void setEs_bulk(Integer es_bulk) {
		this.es_bulk = es_bulk;
	}

	public String getEmail_address() {
		return email_address;
	}

	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getMasscan_path() {
		return masscan_path;
	}

	public void setMasscan_path(String masscan_path) {
		this.masscan_path = masscan_path;
	}

	public String getMasscan_ports() {
		return masscan_ports;
	}

	public void setMasscan_ports(String masscan_ports) {
		this.masscan_ports = masscan_ports;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getPcap4j_network() {
		return pcap4j_network;
	}

	public void setPcap4j_network(String pcap4j_network) {
		this.pcap4j_network = pcap4j_network;
	}
	

}
