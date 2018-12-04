package com.jz.bigdata.common.event.service;

import java.util.List;
import java.util.Map;

import com.jz.bigdata.common.event.entity.Event;

public interface IEventService {
	
	int insert(Event event);

	List<Event> selectAll();

	int updataById(Event event);

	int delete(String id);
	
	List<Event> selectEventByActionId(String actionId);
	
	Map<String,Object> selectEventAction(String id);

}
