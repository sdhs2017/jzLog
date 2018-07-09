package com.jz.bigdata.common.note.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jz.bigdata.common.note.dao.INoteDao;
import com.jz.bigdata.common.note.entity.Note;
import com.jz.bigdata.common.note.service.INoteService;

/**
 * @author shichengyu
 * @date 2017年11月30日 下午2:39:58
 * @description
 */
@Service(value = "NoteService")
public class NoteService implements INoteService {

	@Resource
	private INoteDao noteDao;
	
	
	
	/**
	 * @param note
	 * @return
	 * @description
	 * 添加数据
	 */
	@Override
	public int insert(Note note) {
		return noteDao.insert(note);
	}



	/**
	 * @param account
	 * @param startTime
	 * @param endTime
	 * @return
	 * @description
	 * 数据个数
	 */
	@Override
	public List<String> count(String account, String startTime, String endTime) {
		return noteDao.count(account, startTime, endTime);
	}



	/**
	 * @return
	 * @description
	 * 查询所有数据
	 */
	@Override
	public List<Note> selectAll(Note note) {
		return noteDao.selectAll(note);
	}



	/**
	 * @param ids
	 * @return
	 * @description
	 * 删除数据
	 */
	@Override
	public int delete(String[] ids) {
		return noteDao.delete(ids);
	}



	/**
	 * @return
	 * @description
	 * 删除所有数据
	 */
	@Override
	public int deleteAll() {
		return noteDao.deleteAll();
	}



	/**
	 * @return
	 * @description
	 * 数据备份
	 */
	@Override
	public int backup() {
		List<Note> name=noteDao.tableName();
		
//		获取总数集合
//		listName=(List) name.get(0);
//		System.out.println(listName.get(0));
	
		if(name.size()>0){
			noteDao.dropTable();
		}
		
		return noteDao.backup();
	}



	/**
	 * @return
	 * @description
	 * 数据还原
	 */
	@Override
	public int restore() {
		
		List<Note> name=noteDao.tableName();
		int num=0;
		if(name.size()>0){
		noteDao.dropTableNote();
		num=noteDao.restore();
		}
		
		return num;
	}




}
