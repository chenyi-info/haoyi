package com.hy.otw.dao.sys;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hy.otw.hibernate.utils.HibernateDao;
import com.hy.otw.po.sys.UserResourcesPo;

@Repository
public class UserResourcesDao extends HibernateDao<UserResourcesPo, Long>{

	public void deleteUserAuthority(Long userId) {
		String hql = "delete UserResourcesPo where userId=?";
		this.deleteHql(hql, userId);
	}

	public void batchAddUserAuthority(List<UserResourcesPo> userResourcesPoList) {
		this.batchSave(userResourcesPoList);
	}

	public List<Long> findUserResourcesId(Long userId) {
		String hql = "select menuId from UserResourcesPo where userId=? ";
		return this.find(hql, userId);
	}

}
