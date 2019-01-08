package com.hy.otw.dao.auth;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.hibernate.utils.HibernateDao;
import com.hy.otw.hibernate.utils.Page;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.UserInfoPo;
import com.hy.otw.vo.UserInfoVo;
import com.hy.otw.vo.query.UserInfoQueryVo;

@Repository
public class UserInfoDao extends HibernateDao<UserInfoPo, Long>{

	public void addUserInfo(UserInfoPo UserInfoPo) {
		this.save(UserInfoPo);
	}

	public Pagination findUserInfoList(UserInfoQueryVo userInfoQueryVo) {
		StringBuffer hql = new StringBuffer("from UserInfoPo where delStatus=?");
		List<Object> param = new ArrayList<Object>();
		param.add(DelStatusEnum.NORMAL.getValue());
		Page<UserInfoPo> page = new Page<UserInfoPo>();
		page.setPageNo(userInfoQueryVo.getPage());
		page.setPageSize(userInfoQueryVo.getRows());
		if(StringUtils.isNotBlank(userInfoQueryVo.getContactNumber())){
			hql.append(" and contactNumber like '%").append(userInfoQueryVo.getContactNumber()).append("%'");
		}
		if(StringUtils.isNotBlank(userInfoQueryVo.getUserAccount())){
			hql.append(" and userAccount like '%").append(userInfoQueryVo.getUserAccount()).append("%'");
		}
		if(StringUtils.isNotBlank(userInfoQueryVo.getUserName())){
			hql.append(" and userNumber like '%").append(userInfoQueryVo.getUserName()).append("%'");
		}
		hql.append(" order by createDate desc");
		
		Pagination pagination = this.findPagination(page, hql.toString(), param.toArray());
		return pagination;
	}

	public UserInfoPo getUserInfo(Long userInfoId) {
		String hql = "from UserInfoPo where delStatus=? and id=?";
		UserInfoPo UserInfoPo = this.findUnique(hql, DelStatusEnum.NORMAL.getValue(), userInfoId);
		return UserInfoPo;
	}

	public void editUserInfo(UserInfoPo userInfoPo) {
		this.update(userInfoPo);
	}

	public Boolean checkHasUserInfo(UserInfoVo userInfoVo) {
		StringBuffer hqlBuffer = new  StringBuffer("select count(id) from UserInfoPo where delStatus=? and userAccount=?");
		List<Object> param = new ArrayList<Object>();
		param.add(DelStatusEnum.NORMAL.getValue());
		param.add(userInfoVo.getUserAccount());
		if(userInfoVo.getId() != null){
			hqlBuffer.append(" and id != ?");
			param.add(userInfoVo.getId());
		}
		Query query = this.createQuery(hqlBuffer.toString(), param.toArray());
		Long total = (Long) query.uniqueResult();
		return total > 0l;
	}

	public UserInfoPo getUserInfoByAccount(String account) {
		String hql = "from UserInfoPo where delStatus=? and userAccount=?";
		UserInfoPo UserInfoPo = this.findUnique(hql, DelStatusEnum.NORMAL.getValue(), account);
		return UserInfoPo;
	}
}
