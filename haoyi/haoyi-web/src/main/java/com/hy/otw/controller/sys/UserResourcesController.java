package com.hy.otw.controller.sys;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.hy.otw.service.sys.UserResourcesService;
import com.hy.otw.vo.http.ResponseMsgVo;
import com.hy.otw.vo.sys.MenuResourcesVo;
import com.hy.otw.vo.sys.UserResourcesVo;

@RestController
@RequestMapping("/userResources")
public class UserResourcesController {
	
	@Resource private UserResourcesService userResourcesService;
	
	@RequestMapping(value = "/userAuthority", method = RequestMethod.POST)
	public ResponseMsgVo userAuthority(HttpServletRequest request,HttpServletResponse response, String userResourcesStrList) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			List<UserResourcesVo> userResourcesVoList = JSONArray.parseArray(userResourcesStrList, UserResourcesVo.class);
			userResourcesService.addUserAuthority(userResourcesVoList);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/findUserAuthority", method = RequestMethod.POST)
	public List<MenuResourcesVo> findUserAuthority(HttpServletRequest request,HttpServletResponse response) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return userResourcesService.findUserAuthority();
	}
	
}
