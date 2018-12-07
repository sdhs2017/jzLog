package com.jz.bigdata.common.action.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jz.bigdata.common.action.dao.IActionDao;
import com.jz.bigdata.common.action.entity.Action;
import com.jz.bigdata.common.action.service.IActionService;
import com.jz.bigdata.common.action_event.dao.IAction_eventDao;
import com.jz.bigdata.common.ansj_dic.dao.IAnsj_dicDao;
import com.jz.bigdata.common.ansj_dic.entity.Dic;
import com.jz.bigdata.util.Uuid;


/**
 * @ClassName ActionServiceImpl
 * @Description 
 * @Author shi cheng yu
 * @Date 2018年9月28日 下午2:05:27
 */
@Service(value = "ActionService")
public class ActionServiceImpl implements IActionService{
	
	@Resource
	private IActionDao actionDao;
	@Resource
	private IAction_eventDao action_eventDao;
	@Resource
	private IAnsj_dicDao ansj_dicDao;

	/**
	 * @param action
	 * @return
	 * @description
	 * 添加数据
	 */
	@Override
	public int insert(Action action) {
		action.setId(Uuid.getUUID());
		action.setState(1);
		int result =actionDao.insert(action);
		if(result>0) {
			Dic dics=ansj_dicDao.selectByName(action.getName());
			if(null==dics) {
				Dic dic =new Dic();
				dic.setFreq("1000");
				dic.setNature("userDefine");
				dic.setName(action.getName());
				ansj_dicDao.insert(dic);
			}
			
		}
		return result;
	}

	/**
	 * @param action
	 * @return
	 * @description
	 * 查询数据
	 */
	@Override
	public List<Action> selectAll() {
		return actionDao.selectAll();
	}

	/**
	 * @param action
	 * @return
	 * @description
	 * 修改数据
	 */
	@Override
	public int updateById(Action action) {
		// TODO Auto-generated method stub
		return actionDao.updateById(action);
	}

	/**
	 * @param id
	 * @return
	 * @description
	 * 删除数据
	 */
	@Override
	public int delete(String id) {
		action_eventDao.deleteByActionId(id);
		return actionDao.delete(id);
	}

	/**
	 * @param eventId
	 * @return
	 * @description
	 * 根据eventid查询数据
	 */
	@Override
	public List<Action> selectActionByEventId(String eventId) {
		return actionDao.selectActionByEventId(eventId);
	}

	@Override
	public List<Action> selectAllByType(String type) {
		return actionDao.selectAllByType(type);
	}

}
