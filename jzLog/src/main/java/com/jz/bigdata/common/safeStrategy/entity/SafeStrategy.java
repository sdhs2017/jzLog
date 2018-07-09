package com.jz.bigdata.common.safeStrategy.entity;
/**
 * @author shichengyu
 * @date 2018年3月31日 上午11:11:15
 * @description
 */
public class SafeStrategy {
	
	private String id;
	private String equipmentId;
	private String event_type;
	private int number;
	private String time;
	private int state;
	private String safe_strategy_name;
	private String month;
	private String day;
	private String hour;
	private String minute;
	private String time_interval;
	
	public String getTime_interval() {
		return time_interval;
	}
	public void setTime_interval(String time_interval) {
		this.time_interval = time_interval;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
	public String getEvent_type() {
		return event_type;
	}
	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getSafe_strategy_name() {
		return safe_strategy_name;
	}
	public void setSafe_strategy_name(String safe_strategy_name) {
		this.safe_strategy_name = safe_strategy_name;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getMinute() {
		return minute;
	}
	public void setMinute(String minute) {
		this.minute = minute;
	}
	
	

}
