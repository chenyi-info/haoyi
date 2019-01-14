package com.hy.otw.dao.sys;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.hibernate.utils.HibernateDao;
import com.hy.otw.po.sys.MenuResourcesPo;
import com.hy.otw.vo.query.sys.MenuResourcesQueryVo;

@Repository
public class MenuResourcesDao extends HibernateDao<MenuResourcesPo, Long>{

	public void addMenuResources(MenuResourcesPo MenuResourcesPo) {
		this.save(MenuResourcesPo);
	}

	public List<MenuResourcesPo> findMenuResourcesList(MenuResourcesQueryVo MenuResourcesQueryVo) {
		StringBuffer hql = new StringBuffer("from MenuResourcesPo where delStatus=?");
		List<Object> param = new ArrayList<Object>();
		param.add(DelStatusEnum.NORMAL.getValue());
		hql.append(" order by sort asc");
		return this.find(hql.toString(), param);
	}

	public MenuResourcesPo getMenuResources(Long MenuResourcesId) {
		String hql = "from MenuResourcesPo where delStatus=? and id=?";
		MenuResourcesPo MenuResourcesPo = this.findUnique(hql, DelStatusEnum.NORMAL.getValue(), MenuResourcesId);
		return MenuResourcesPo;
	}

	public void editMenuResources(MenuResourcesPo menuResourcesPo) {
		this.update(menuResourcesPo);
	}

	public MenuResourcesPo getMenuResourcesByFunCode(String funCode) {
		String hql = "from MenuResourcesPo where delStatus=? and funCode=?";
		MenuResourcesPo MenuResourcesPo = this.findUnique(hql, DelStatusEnum.NORMAL.getValue(), funCode);
		return MenuResourcesPo;
	}

	public List<MenuResourcesPo> findMenuResourcesListByIds(List<Long> userResourcesIdList) {
		String hql = "from MenuResourcesPo where delStatus=:delStatus and status = 1 and id in (:userResourcesIdList)";
		Query query = this.createQuery(hql);
		query.setParameter("delStatus", DelStatusEnum.NORMAL.getValue());
		query.setParameterList("userResourcesIdList", userResourcesIdList);
		return query.list();
	}
}
