package com.jz.bigdata.common.masscanip.service;

import java.util.List;

import com.jz.bigdata.common.masscanip.entity.Masscanip;

public interface IMasscanipService {
	int insert(Masscanip masscanip);

	List<Masscanip> selectAll();

	int updateById(Masscanip masscanip);

	int delete(String id);

}
