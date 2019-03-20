package com.hy.otw.service.sys;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.dao.sys.UserResourcesDao;
import com.hy.otw.po.sys.UserResourcesPo;
import com.hy.otw.vo.UserInfoVo;
import com.hy.otw.vo.sys.MenuResourcesVo;
import com.hy.otw.vo.sys.UserResourcesVo;

@Service
public class UserResourcesService {
	
	@Resource private UserResourcesDao userResourcesDao;
	@Resource private MenuResourcesService menuResourcesService;

	public void addUserAuthority(Long userId, List<UserResourcesVo> userResourcesVoList) throws Exception {
		if(CollectionUtils.isEmpty(userResourcesVoList)){
			throw new Exception("权限菜单不能为空");
		}
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		userResourcesDao.deleteUserAuthority(userId);
		Date date = new Date();
		List<UserResourcesPo> userResourcesPoList = new ArrayList<UserResourcesPo>();
		for (UserResourcesVo userResourcesVo : userResourcesVoList) {
			UserResourcesPo userResourcesPo = new UserResourcesPo();
			PropertyUtils.copyProperties(userResourcesPo, userResourcesVo);
			userResourcesPo.setCreateBy(loginUser.getId());
			userResourcesPo.setCreateDate(date);
			userResourcesPo.setUpdateBy(loginUser.getId());
			userResourcesPo.setUpdateDate(date);
			userResourcesPo.setDelStatus(DelStatusEnum.NORMAL.getValue());
			userResourcesPoList.add(userResourcesPo);
		}
		userResourcesDao.batchAddUserAuthority(userResourcesPoList);
	}


	public List<MenuResourcesVo> findUserAuthority() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		List<Long> userResourcesIdList =  this.userResourcesDao.findUserResourcesId(loginUser.getId());
		List<MenuResourcesVo> menuResourcesVoList = null;
		if(CollectionUtils.isNotEmpty(userResourcesIdList)){
			menuResourcesVoList = menuResourcesService.findMenuResourcesListByIds(userResourcesIdList);
		}
		
		return menuResourcesVoList;
	}


	public List<Long> getUserResources(Long userId) {
		return this.userResourcesDao.findUserResourcesId(userId);
	}

}
