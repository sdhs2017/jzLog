package com.jz.bigdata.common.event.dao;

import java.util.List;

import com.jz.bigdata.common.event.entity.Event;

public interface IEventDao {
	int insert(Event event);

	List<Event> selectAll();

	int updataById(Event event);

	int delete(String id);
	
	List<Event> selectEventByActionId(String actionId);
	
	Event selectEvent(String id);
	
	

}
