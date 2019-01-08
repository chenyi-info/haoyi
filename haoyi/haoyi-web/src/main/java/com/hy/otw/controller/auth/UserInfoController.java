package com.hy.otw.controller.auth;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.service.auth.UserInfoService;
import com.hy.otw.vo.UserInfoVo;
import com.hy.otw.vo.http.ResponseMsgVo;
import com.hy.otw.vo.query.UserInfoQueryVo;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
	
@Resource private UserInfoService userInfoService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseMsgVo add(HttpServletRequest request,HttpServletResponse response, UserInfoVo userInfoVo) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.userInfoService.addUserInfo(userInfoVo);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ResponseMsgVo edit(HttpServletRequest request,HttpServletResponse response, UserInfoVo userInfoVo) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.userInfoService.editUserInfo(userInfoVo);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseMsgVo delete(HttpServletRequest request,HttpServletResponse response, Long userInfoId) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.userInfoService.deleteUserInfo(userInfoId);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Pagination list(HttpServletRequest request,HttpServletResponse response, UserInfoQueryVo userInfoQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.userInfoService.findUserInfoList(userInfoQueryVo);
		return pagination;
	}
}
