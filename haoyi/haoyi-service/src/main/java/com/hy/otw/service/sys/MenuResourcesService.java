package com.hy.otw.service.sys;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.dao.sys.MenuResourcesDao;
import com.hy.otw.po.sys.MenuResourcesPo;
import com.hy.otw.vo.query.sys.MenuResourcesQueryVo;
import com.hy.otw.vo.sys.MenuResourcesVo;

@Service
public class MenuResourcesService {
	
	@Resource private MenuResourcesDao menuResourcesDao;

	public void addMenuResources(MenuResourcesVo menuResourcesVo) throws Exception {
		MenuResourcesPo menuResourcesPo = menuResourcesDao.getMenuResourcesByFunCode(menuResourcesVo.getFunCode());
		if(menuResourcesPo != null){
			throw new Exception("菜单编码不能重复");
		}
		Date date = new Date();
		menuResourcesPo = new MenuResourcesPo();
		menuResourcesVo.setCreateBy(1l);
		menuResourcesVo.setCreateDate(date);
		menuResourcesVo.setUpdateBy(1l);
		menuResourcesVo.setUpdateDate(date);
		menuResourcesVo.setDelStatus(DelStatusEnum.NORMAL.getValue());
		PropertyUtils.copyProperties(menuResourcesPo, menuResourcesVo);
		this.menuResourcesDao.addMenuResources(menuResourcesPo);
	}

	public List<MenuResourcesVo> findMenuResourcesList(MenuResourcesQueryVo menuResourcesQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<MenuResourcesPo> menuResourcePoList =  this.menuResourcesDao.findMenuResourcesList(menuResourcesQueryVo);
		List<MenuResourcesVo> menuResourceVoList = new ArrayList<MenuResourcesVo>();
		if(CollectionUtils.isNotEmpty(menuResourcePoList)){
			for (MenuResourcesPo menuResourcesPo : menuResourcePoList) {
				MenuResourcesVo menuResourcesVo = new MenuResourcesVo();
				PropertyUtils.copyProperties(menuResourcesVo, menuResourcesPo);
				menuResourceVoList.add(menuResourcesVo);
			}
		}
		return menuResourceVoList;
	}

	public void editMenuResources(MenuResourcesVo menuResourcesVo) throws Exception {
		MenuResourcesPo menuResourcesPo = menuResourcesDao.getMenuResourcesByFunCode(menuResourcesVo.getFunCode());
		if(menuResourcesPo != null && menuResourcesPo.getId() != menuResourcesVo.getId()){
			throw new Exception("菜单编码不能重复");
		}
		if(menuResourcesPo == null){
			menuResourcesPo = menuResourcesDao.getMenuResources(menuResourcesVo.getId());
		}
		if(menuResourcesPo == null){
			throw new Exception("未找到该项信息");
		}
		Date date = new Date();
		menuResourcesVo.setCreateBy(menuResourcesPo.getCreateBy());
		menuResourcesVo.setCreateDate(menuResourcesPo.getCreateDate());
		menuResourcesVo.setUpdateBy(1l);
		menuResourcesVo.setUpdateDate(date);
		menuResourcesVo.setDelStatus(menuResourcesPo.getDelStatus());
		PropertyUtils.copyProperties(menuResourcesPo, menuResourcesVo);
		this.menuResourcesDao.editMenuResources(menuResourcesPo);
	}

	public void deleteMenuResources(Long menuResourcesId) throws Exception {
		MenuResourcesPo menuResourcesPo = menuResourcesDao.getMenuResources(menuResourcesId);
		if(menuResourcesPo == null){
			throw new Exception("未找到司机信息");
		}
		Date date = new Date();
		menuResourcesPo.setDelStatus(DelStatusEnum.HIDE.getValue());
		menuResourcesPo.setUpdateBy(1l);
		menuResourcesPo.setUpdateDate(date);
		this.menuResourcesDao.editMenuResources(menuResourcesPo);
	}

	public List<MenuResourcesVo> findMenuResourcesListByIds(List<Long> userResourcesIdList) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<MenuResourcesPo> menuResourcePoList = menuResourcesDao.findMenuResourcesListByIds(userResourcesIdList);
		List<MenuResourcesVo> menuResourceVoList = new ArrayList<MenuResourcesVo>();
		if(CollectionUtils.isNotEmpty(menuResourcePoList)){
			for (MenuResourcesPo menuResourcesPo : menuResourcePoList) {
				MenuResourcesVo menuResourcesVo = new MenuResourcesVo();
				PropertyUtils.copyProperties(menuResourcesVo, menuResourcesPo);
				menuResourceVoList.add(menuResourcesVo);
			}
		}
		return menuResourceVoList;
	}

}
