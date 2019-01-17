package com.hy.otw.service.auth;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.dao.auth.UserInfoDao;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.UserInfoPo;
import com.hy.otw.utils.MD5Util;
import com.hy.otw.vo.UserInfoVo;
import com.hy.otw.vo.query.UserInfoQueryVo;

@Service
public class UserInfoService {
	
	@Resource private UserInfoDao userInfoDao;
	
	private final char[] randomChars = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};

	public void addUserInfo(UserInfoVo userInfoVo) throws Exception {
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		UserInfoPo userInfoPo = userInfoDao.getUserInfoByAccount(userInfoVo.getUserAccount());
		if(userInfoPo != null) {
			throw new Exception("账号不能重复");
		}
		String salt = RandomStringUtils.random(10, randomChars);
		Date date = new Date();
		userInfoPo = new UserInfoPo();
		userInfoVo.setPassword(MD5Util.md5(userInfoVo.getPassword(), salt));
		userInfoVo.setSalt(salt);
		userInfoVo.setCreateBy(loginUser.getId());
		userInfoVo.setCreateDate(date);
		userInfoVo.setUpdateBy(loginUser.getId());
		userInfoVo.setUpdateDate(date);
		userInfoVo.setDelStatus(DelStatusEnum.NORMAL.getValue());
		PropertyUtils.copyProperties(userInfoPo, userInfoVo);
		this.userInfoDao.addUserInfo(userInfoPo);
	}

	public Pagination findUserInfoList(UserInfoQueryVo userInfoQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.userInfoDao.findUserInfoList(userInfoQueryVo);
		List<UserInfoPo> userInfoPoList = (List<UserInfoPo>) pagination.getRows();
		List<UserInfoVo> userInfoVoList = new ArrayList<UserInfoVo>();
		if(CollectionUtils.isNotEmpty(userInfoPoList)){
			for (UserInfoPo userInfoPo : userInfoPoList) {
				UserInfoVo userInfoVo = new UserInfoVo();
				PropertyUtils.copyProperties(userInfoVo, userInfoPo);
				userInfoVoList.add(userInfoVo);
			}
		}
		pagination.setRows(userInfoVoList);
		return pagination;
	}


	public void editUserInfo(UserInfoVo userInfoVo) throws Exception {
		UserInfoPo userInfoPo = userInfoDao.getUserInfoByAccount(userInfoVo.getUserAccount());
		if(userInfoPo != null && userInfoPo.getId() != userInfoVo.getId()){
			throw new Exception("账号不能重复");
		}
		if(userInfoPo == null){
			userInfoPo = userInfoDao.getUserInfo(userInfoVo.getId());
		}
		if(userInfoPo == null){
			throw new Exception("未找到该条信息");
		}
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		Date date = new Date();
		userInfoVo.setPassword(userInfoPo.getPassword());
		userInfoVo.setSalt(userInfoPo.getSalt());
		userInfoVo.setCreateBy(userInfoPo.getCreateBy());
		userInfoVo.setCreateDate(userInfoPo.getCreateDate());
		userInfoVo.setUpdateBy(loginUser.getId());
		userInfoVo.setUpdateDate(date);
		userInfoVo.setDelStatus(userInfoPo.getDelStatus());
		PropertyUtils.copyProperties(userInfoPo, userInfoVo);
		this.userInfoDao.editUserInfo(userInfoPo);
	}
	
	public void deleteUserInfo(Long userId) throws Exception {
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		UserInfoPo userInfoPo = userInfoDao.getUserInfo(userId);
		if(userInfoPo == null){
			throw new Exception("未找到该条信息");
		}
		Date date = new Date();
		userInfoPo.setDelStatus(DelStatusEnum.HIDE.getValue());
		userInfoPo.setUpdateBy(loginUser.getId());
		userInfoPo.setUpdateDate(date);
		this.userInfoDao.editUserInfo(userInfoPo);
	}

	public UserInfoVo getUserInfoByAccount(String userAccount) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		UserInfoPo userInfoPo = userInfoDao.getUserInfoByAccount(userAccount);
		UserInfoVo userInfoVo = new UserInfoVo();
		if(userInfoPo != null){
			PropertyUtils.copyProperties(userInfoVo, userInfoPo);
		}
		return userInfoVo;
	}

}
