package com.jz.bigdata.common.masscanip.dao;

import java.util.List;

import com.jz.bigdata.common.masscanip.entity.Masscanip;

public interface IMasscanipDao {
	int insert(Masscanip masscanip);

	List<Masscanip> selectAll();

	int updateById(Masscanip masscanip);

	int delete(String id);

}
