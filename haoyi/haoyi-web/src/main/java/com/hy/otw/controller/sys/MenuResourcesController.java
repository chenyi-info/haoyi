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
import com.alibaba.fastjson.JSONObject;
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
	public List<MenuResourcesVo> list(HttpServletRequest request,HttpServletResponse response, MenuResourcesQueryVo menuResourcesQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return this.menuResourcesService.findMenuResourcesList(menuResourcesQueryVo);
	}
	
	@RequestMapping(value = "/findMenuList", method = RequestMethod.GET)
	public JSONArray findMenuList(HttpServletRequest request,HttpServletResponse response, MenuResourcesQueryVo menuResourcesQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<MenuResourcesVo> menuList = this.menuResourcesService.findMenuResourcesList(menuResourcesQueryVo);
		JSONArray jsonArray = new JSONArray();
		for (MenuResourcesVo menuResourcesVo : menuList) {
			if (menuResourcesVo.getParentId() < 1) {
				menuResourcesVo.setParentId(Long.valueOf(-1));
				JSONArray treeMenu = new JSONArray();
				String pId = menuResourcesVo.getParentId().toString();

				String menuId = menuResourcesVo.getId().toString();
				JSONObject jsonMenu = new JSONObject();
				jsonMenu.put("id", menuId);
				jsonMenu.put("parentMenuId", pId);
				jsonMenu.put("menuName", menuResourcesVo.getMenuName());
				jsonMenu.put("proName", menuResourcesVo.getMenuName());
				JSONArray c_node = setTreeJSONArray(menuList, menuId);
				if (c_node.size() > 0) {
					jsonMenu.put("childNode", c_node);
				}
				treeMenu.add(jsonMenu);
				jsonArray.addAll(treeMenu);
			}
		}
		return jsonArray;
	}
	

	/**
	 * 将数组集合转换为树形集合并排除掉已关闭菜单
	 * @param list
	 * @param listProject
	 * @param parentId
	 * @param roletype
	 * @return
	 */
	private JSONArray setTreeJSONArray(List<MenuResourcesVo> list, String parentId) {
		JSONArray treeMenu = new JSONArray();
		for (MenuResourcesVo menuResourcesVo : list) {
			String pId = menuResourcesVo.getParentId().toString();
			String menuStatus = menuResourcesVo.getStatus().toString();
			if (parentId.equals(pId) && "1".equals(menuStatus)) {
				String menuId = menuResourcesVo.getId().toString();
				JSONObject jsonMenu = new JSONObject();
				jsonMenu.put("id", menuId);
				jsonMenu.put("parentMenuId", pId);
				jsonMenu.put("menuName", menuResourcesVo.getMenuName());
				jsonMenu.put("proName", menuResourcesVo.getMenuName());
				JSONArray c_node = setTreeJSONArray(list, menuId);
				if (c_node.size() > 0) {
					jsonMenu.put("childNode", c_node);
				}
				treeMenu.add(jsonMenu);
			}
		}
		return treeMenu;
	}
}
