package com.jz.bigdata.common.masscanip.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jz.bigdata.common.masscanip.dao.IMasscanipDao;
import com.jz.bigdata.common.masscanip.entity.Masscanip;
import com.jz.bigdata.common.masscanip.service.IMasscanipService;

@Service(value = "MasscanipService")
public class MasscanipServiceimpl implements IMasscanipService{
	
	@Resource
	private IMasscanipDao masscanipDao;

	@Override
	public int insert(Masscanip masscanip) {
		// TODO Auto-generated method stub
		return masscanipDao.insert(masscanip);
	}

	@Override
	public List<Masscanip> selectAll() {
		// TODO Auto-generated method stub
		return masscanipDao.selectAll();
	}

	@Override
	public int updateById(Masscanip masscanip) {
		// TODO Auto-generated method stub
		return masscanipDao.updateById(masscanip);
	}

	@Override
	public int delete(String id) {
		// TODO Auto-generated method stub
		return masscanipDao.delete(id);
	}
	


}
