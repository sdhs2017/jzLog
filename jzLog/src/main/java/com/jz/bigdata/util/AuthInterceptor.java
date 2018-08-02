package com.jz.bigdata.util;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.jz.bigdata.common.Constant;
import com.jz.bigdata.common.function.dao.IFunctionDao;
import com.jz.bigdata.common.function.entity.Function;
import com.jz.bigdata.common.function.service.IFunctionService;
import com.jz.bigdata.common.function.service.impl.FunctionServiceImpl;
import com.jz.bigdata.common.function.util.GetfunctionMap;
import com.jz.bigdata.common.users.dao.IUsersDao;
import com.jz.bigdata.common.users.entity.User;
import com.jz.bigdata.common.users.service.IUsersService;
import com.jz.bigdata.common.users.service.impl.UsersServiceImpl;

import net.sf.json.JSONArray;

/**
 * @author yiyang
 * @date 2016年7月29日 上午10:48:15
 * @description controller请求拦截器，请求到达controller前
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {
	
	@Resource(name = "UsersService")
	private IUsersService userService;
	
	@Resource(name = "FunctionService")
	private IFunctionService functionService;
	
	@Resource
	private IUsersDao userDao;
	
	@Resource
	private IFunctionDao functionDao;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    	//如果是登陆请求，放行
    	if(handler.toString().indexOf(Constant.LOGINPATH)>=0||handler.toString().indexOf(Constant.REGISTERPATH)>=0||handler.toString().indexOf(Constant.uploadPATH)>=0){
    		if(request.getRequestURI().equals("/jzLog/users/logout.do")||request.getRequestURI().equals("/jzLog/users/registerUser.do")||request.getRequestURI().equals("/jzLog/upload/licenseUpload.do")){
    			return true;
    		}else{
    		String phone=request.getParameter("phone");
    		String password=request.getParameter("password");
    		User user =new User();
    		user.setPhone(phone);
    		user.setPassword(MD5.EncoderByMd5(password));
    		List<User> _userList = userDao.selectByPhonePwd(user);
    		
//    		System.out.println(_userList.get(0).getName());
    		GetfunctionMap getfunctionMap= new GetfunctionMap();
//    		getfunctionMap.map;
    		Map<String,Map<String,String>> map=GetfunctionMap.map;
//    		System.out.println(map.get(_userList.get(0).getRole()));
    		
    		if(_userList.size()<1){
    			return true;
    		}else{
    		if(map.get(String.valueOf(_userList.get(0).getRole()) )==null||map.get(String.valueOf(_userList.get(0).getRole())).equals("")){
    			getfunctionMap.getfunctionMap(_userList.get(0).getRole(),functionService);
    		}
    		}
//    		System.out.println(GetfunctionMap.map.size());
//    		funService.map;
//    		functionService.getClass()
//    		functionService
//    	    functionService.selectAll(_userList.get(0).getRole());
    	    
//    		System.err.println(request.getRequestURI());
//    		PrintWriter out = response.getWriter();
//    		response.setCharacterEncoding("utf-8");
//            out.print("您没有权限");  
             
    		return true;
    		}
    	}else{
    		//session超时检测
        	HttpSession session = request.getSession();
        	//session userid不为空且sessionid一致
//        	System.err.println(session.getAttribute(Constant.SESSION_USERID)!=null);
//        	System.err.println(session.getId().equals(session.getAttribute(Constant.SESSION_ID)));
//        	System.err.println(session.getAttribute(Constant.SESSION_USERID));
//        	System.err.println(session.getAttribute(Constant.SESSION_ID));
        	if(session.getAttribute(Constant.SESSION_USERID)!=null&&session.getId().equals(session.getAttribute(Constant.SESSION_ID))){
//        		System.err.println(session.getAttribute(Constant.SESSION_USERID));
        		Map<String,Map<String,String>> map=GetfunctionMap.map;
//        		System.out.println(Constant.SESSION_USERROLE);
//        		System.out.println(session.getAttribute(Constant.SESSION_USERROLE));
//        		System.out.println(map.get(session.getAttribute(Constant.SESSION_USERROLE).toString()));
//        		System.out.println(request.getRequestURI().toString());
        		if(map!=null&&map.get(session.getAttribute(Constant.SESSION_USERROLE).toString()).get(request.getRequestURI().toString())!=null){
        			return true;
        			
        		}else{
        			response.setContentType("text/html;charset=utf-8");
        			PrintWriter out = response.getWriter();
//        			JSONObject jsStr = JSONObject.parseObject("{msg:您没有权限}");
//        			JSONArray array=JSONArray.fromObject("{msg:您没有权限}");
//            		response.setCharacterEncoding("utf-8");
                    out.print("您没有权限");  
                    
        			return false;
        		}
//        		System.out.println(map.size());
//        		System.out.println(map.get(session.getAttribute(Constant.SESSION_USERROLE)).get("/jzLog/users/selectUser.do"));
//        		return true;
        	}else{
        		response.setContentType("text/html;charset=utf-8");
    			PrintWriter out = response.getWriter();
//    			JSONObject jsStr = JSONObject.parseObject("{msg:您没有权限}");
//    			JSONArray array=JSONArray.fromObject("{msg:您没有权限}");
//        		response.setCharacterEncoding("utf-8");
                out.print("{\"success\":\"false\",\"message\":\"登录超时，请刷新页面重新登录\"}");  
                return false;
            }     
    	}
          
     }
    

}