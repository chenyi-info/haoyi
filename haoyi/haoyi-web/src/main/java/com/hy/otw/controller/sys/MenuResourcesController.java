package com.hy.otw.controller.sys;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.service.sys.MenuResourcesService;
import com.hy.otw.vo.http.ResponseMsgVo;
import com.hy.otw.vo.query.sys.MenuResourcesQueryVo;
import com.hy.otw.vo.sys.MenuResourcesVo;

@RestController
@RequestMapping("/menuResources")
public class MenuResourcesController {
	
@Resource private MenuResourcesService menuResourcesService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseMsgVo add(HttpServletRequest request,HttpServletResponse response, MenuResourcesVo menuResourcesVo) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.menuResourcesService.addMenuResources(menuResourcesVo);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ResponseMsgVo edit(HttpServletRequest request,HttpServletResponse response, MenuResourcesVo menuResourcesVo) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.menuResourcesService.editMenuResources(menuResourcesVo);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseMsgVo delete(HttpServletRequest request,HttpServletResponse response, Long menuResourcesId) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.menuResourcesService.deleteMenuResources(menuResourcesId);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Pagination list(HttpServletRequest request,HttpServletResponse response, MenuResourcesQueryVo menuResourcesQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.menuResourcesService.findMenuResourcesList(menuResourcesQueryVo);
		return pagination;
	}
}
