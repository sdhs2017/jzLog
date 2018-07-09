package com.jz.bigdata.common.note.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jz.bigdata.common.note.entity.Note;

/**
 * @author shichengyu
 * @date 2017年11月30日 下午2:39:34
 * @description
 */
public interface INoteService {
	int insert(Note note);
	
	List<String> count(String account,String startTime,String endTime);
	
	List<Note> selectAll(Note note);
	
	int delete(String[] ids);
	
	int deleteAll();
	
	int backup();
	
	int restore();

}
