package com.jz.bigdata.common.users.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.jz.bigdata.common.Constant;
import com.jz.bigdata.common.department.dao.IDepartmentDao;
import com.jz.bigdata.common.department.entity.Department;
import com.jz.bigdata.common.license.VerifyLicense;
import com.jz.bigdata.common.note.dao.INoteDao;
import com.jz.bigdata.common.note.entity.Note;
import com.jz.bigdata.common.users.dao.IUsersDao;
import com.jz.bigdata.common.users.entity.User;
import com.jz.bigdata.common.users.service.IUsersService;
import com.jz.bigdata.common.users.util.Page;
import com.jz.bigdata.util.MD5;
import com.jz.bigdata.util.Uuid;

/**
 * @author shichengyu
 * @date 2017年8月1日 上午10:06:51
 * @description 用户管理相关模块
 */
@Service(value="UsersService")
public class UsersServiceImpl implements IUsersService{
	@Resource
	private IUsersDao userDao;
	
	@Resource
	private IDepartmentDao departmentDao;
	
	@Resource
	private INoteDao noteDao;
	
	@Resource(name = "licenseService")
	private VerifyLicense verifyLicense;

	/**
	 * @param user
	 * @return
	 * @description
	 * 添加数据
	 */
	public int insert(User user) {
		user.setState(1);
		user.setPassword(MD5.EncoderByMd5(user.getPassword()));
		return userDao.insert(user);
	}


	/**
	 * @return
	 * @description
	 * 查询所有数据
	 */
	public List<User> selectAll(User user) {
		return userDao.selectAll(user);
	}

	/**
	 * @param ids
	 * @return
	 * @description
	 * 根据id删除数据
	 */
	@Override
	public int delete(String[] ids) {
		return userDao.delete(ids);
	}

	/**
	 * @param user
	 * @return
	 * @description
	 * 修改数据
	 * 
	 */
	@Override
	public int updateById(User user) {
		int tt= userDao.updateById(user);
//		int t=1/0;
		return tt;
	}


	/**
	 * @param page
	 * @return
	 * @description
	 * 分页查询
	 */
	@Override
	public Map<String,Object> selectPage(Page page,HttpSession session) {
//		开始数
		page.setStartRecord(page.getPageSize()*(page.getPageIndex()-1));
		page.setRole(Integer.valueOf((String) session.getAttribute(Constant.SESSION_USERROLE)));
		page.setId((String) session.getAttribute(Constant.SESSION_USERID));
//		总数
		List<String> count=userDao.count(page);
		Map<String,Object> map =new HashMap<String,Object>();
//		List li=new ArrayList<>();
//		添加到map
		map.put("count", (count.get(0)));
//		List<Map<String,Object>> list=new ArrayList<>();
//		用户信息添加到map
		List<User> listUser= userDao.selectPage(page);
		
		map.put("user", listUser);
//		list.add(map);
		return map;  
	}


	/**
	 * @param id
	 * @return
	 * @description
	 * 通过id查询单个用户
	 */
	@Override
	public List<User> selectUser(String id) {
		return userDao.selectUser(id);
	}


	/**
	 * @param id
	 * @return
	 * @description
	 * 根据id返回单个数据信息
	 */
	@Override
	public User selectById(String id) {
		return userDao.selectById(id);
	}

	/**
	 * @param user
	 * @param session
	 * @return 是否登陆成功 true/false
	 * @description 登陆操作
	 */
	public int login(User user,HttpSession session){
		//查询账号密码对应的用户信息
		user.setPassword(MD5.EncoderByMd5(user.getPassword()));
		List<User> _userList = userDao.selectByPhonePwd(user);
//		int tt= 1/0;
		int result=0;
		//获取参数
		verifyLicense.setParam("/verifyparam.properties");
		//验证证书
		Boolean vresult = verifyLicense.verify();
		//证书是否存在
		if (vresult) {
			//用户是否存在
			if(_userList.size()==1){
				User _user = _userList.get(0);
				//用户是否可用
				if(_user.getState()==1){
					Department department= departmentDao.selectDepartment((_user.getDepartmentId())+"");
					//用户是否存在
					if(_user.getId()!=null){
						session.setAttribute(Constant.SESSION_USERID, _user.getId());
						session.setAttribute(Constant.SESSION_USERNAME, _user.getName());
						session.setAttribute(Constant.SESSION_USERACCOUNT, _user.getPhone());
						//是否有所属部门
						if(_user.getDepartmentId()!=0){
							session.setAttribute(Constant.SESSION_DEPARTMENTNAME, department.getName());
							session.setAttribute(Constant.SESSION_DEPARTMENTID, _user.getDepartmentId());
							session.setAttribute(Constant.SESSION_DEPARTMENTNODEID, department.getDepartmentNodeId());
						}
						
						session.setAttribute(Constant.SESSION_ID, session.getId());
						session.setAttribute(Constant.SESSION_USERROLE, String.valueOf(_user.getRole()));
//						return "{\"success\":\"true\",\"message\":\"登陆成功\"}";
						result=1;
						return result;
					}
				//账号被锁定
				}else{
					result=3;
					return result;
				}
				
			}
			Date date=new Date();
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
			String startTime =df.format(new Date(date.getTime() -  30 * 60 * 1000));
			String endTime=df.format(date);
			//判断登录密码次数过多，锁定账号
			List<Note> list=noteDao.selectLimitNote(user.getPhone(), startTime, endTime);
			Boolean res = false;
			if(list.size()==5){
				for(int i=0;i<list.size();i++){
					if(list.get(i).getResult()==1){
						res=true;
					}
				}
				//修改状态
				if(res==false){
					userDao.updateByPhone(user.getPhone());	
				}
			}
			result=2;
			return result;
		//无授权
		}else {
			return 4;
		}
		
	}
	
	public static void main(String[] agrs){
		
	   Date d=new Date();   
	   SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
	   System.out.println("今天的日期："+df.format(d));   
	   System.out.println("两天前的日期：" + df.format(new Date(d.getTime() -  30 * 60 * 1000)));  
	   System.out.println("三天后的日期：" + df.format(new Date(d.getTime() + 3 * 24 * 60 * 60 * 1000)));
	}
	

	/**
	 * @param session
	 * @return
	 * @description 验证session 信息
	 */
	public Boolean checkLogin(HttpSession session) {
		if(session.getAttribute(Constant.SESSION_USERID)!=null&&session.getId().equals(session.getAttribute(Constant.SESSION_ID))){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * @param session
	 * @return
	 * @description 登陆，清空session
	 */
	public Boolean loginOut(HttpSession session) {
		session.removeAttribute(Constant.SESSION_ID);
		session.removeAttribute(Constant.SESSION_USERID);
		session.removeAttribute(Constant.SESSION_DEPARTMENTID);
		return true;
	}


	@Override
	public List<User> selectUserRole(HttpSession session) {
		
		return userDao.selectUserRole(session.getAttribute(Constant.SESSION_USERID).toString());
	}


	/**
	 * @param user
	 * @return
	 * @description
	 * 用户注册
	 */
	@Override
	public int registerUser(User user) {
		user.setPassword(MD5.EncoderByMd5(user.getPassword()));
		user.setId(Uuid.getUUID());
		return   userDao.registerUser(user);
	}

	/**
	 * @param user
	 * @return
	 * @description
	 * 查询用户是否已经存在
	 */
	@Override
	public List<User> selectByName(User user) {
		// TODO Auto-generated method stub
		return userDao.selectByName(user);
	}


	/**
	 * @param user
	 * @return
	 * @description
	 * 修改密码
	 */
	@Override
	public String updatePasswordById(String id,String password,String oldPassword) {
		int result=0;
		String pwd=MD5.EncoderByMd5(password);
		String oldPwd=MD5.EncoderByMd5(oldPassword);
		List<User> user=userDao.selectByPasswordId(id, oldPwd);
		if(user.size()>0){
			result=userDao.updatePasswordById(id,pwd);
		}
		
		return result >= 1 ? Constant.successMessage() : Constant.updateUserPasswordMessage();
	}
}
