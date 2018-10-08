package com.jz.bigdata.common.action.dao;

import java.util.List;

import com.jz.bigdata.common.action.entity.Action;

public interface IActionDao {

	int insert(Action action);

	List<Action> selectAll();

	int updateById(Action action);

	int delete(String id);
	
	List<Action> selectActionByEventId(String eventId);

}
