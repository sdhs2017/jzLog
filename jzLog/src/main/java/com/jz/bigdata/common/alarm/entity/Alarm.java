package com.jz.bigdata.common.alarm.entity;
/**
 * @author shichengyu
 * @date 2018年2月28日 下午12:35:59
 * @description
 */
public class Alarm {
	private String id;
	private String event_type;
	private int emailState;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getEvent_type() {
		return event_type;
	}
	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}
	public int getEmailState() {
		return emailState;
	}
	public void setEmailState(int emailState) {
		this.emailState = emailState;
	}
	

}
