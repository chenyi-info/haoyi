package com.hy.otw.dao.sys;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.hibernate.utils.HibernateDao;
import com.hy.otw.hibernate.utils.Page;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.sys.MenuResourcesPo;
import com.hy.otw.vo.query.sys.MenuResourcesQueryVo;

@Repository
public class MenuResourcesDao extends HibernateDao<MenuResourcesPo, Long>{

	public void addMenuResources(MenuResourcesPo MenuResourcesPo) {
		this.save(MenuResourcesPo);
	}

	public Pagination findMenuResourcesList(MenuResourcesQueryVo MenuResourcesQueryVo) {
		StringBuffer hql = new StringBuffer("from MenuResourcesPo where delStatus=?");
		List<Object> param = new ArrayList<Object>();
		param.add(DelStatusEnum.NORMAL.getValue());
		Page<MenuResourcesPo> page = new Page<MenuResourcesPo>();
		page.setPageNo(MenuResourcesQueryVo.getPage());
		page.setPageSize(MenuResourcesQueryVo.getRows());
		hql.append(" order by updateDate desc");
		Pagination pagination = this.findPagination(page, hql.toString(), param.toArray());
		return pagination;
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
}
