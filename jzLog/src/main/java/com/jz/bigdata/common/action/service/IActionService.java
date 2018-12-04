package com.jz.bigdata.common.action.service;

import java.util.List;

import com.jz.bigdata.common.action.entity.Action;

public interface IActionService {
	
	int insert(Action action);

	List<Action> selectAll();

	int updateById(Action action);

	int delete(String id);
	
	List<Action> selectActionByEventId(String eventId);
	
	List<Action> selectAllByType(String type);

}
