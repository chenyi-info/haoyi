package com.hy.otw.dao.dictionary;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.hibernate.utils.HibernateDao;
import com.hy.otw.hibernate.utils.Page;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.DictionaryItemPo;
import com.hy.otw.vo.query.DictionaryQueryVo;

@Repository
public class DictionaryItemDao extends HibernateDao<DictionaryItemPo, Long>{

	public void addDictionaryItem(DictionaryItemPo dictionaryItemPo) {
		this.save(dictionaryItemPo);
	}

	public Pagination findDictionaryItemList(DictionaryQueryVo dictionaryQueryVo) {
		StringBuffer hql = new StringBuffer("from DictionaryItemPo where delStatus=?");
		List<Object> param = new ArrayList<Object>();
		param.add(DelStatusEnum.NORMAL.getValue());
		Page<DictionaryItemPo> page = new Page<DictionaryItemPo>();
		page.setPageNo(dictionaryQueryVo.getPage());
		page.setPageSize(dictionaryQueryVo.getRows());
		if(StringUtils.isNotBlank(dictionaryQueryVo.getDictionaryCode())){
			hql.append(" and dictionaryCode = ?");
			param.add(dictionaryQueryVo.getDictionaryCode());
		}
		hql.append(" order by updateDate desc");
		
		Pagination pagination = this.findPagination(page, hql.toString(), param.toArray());
		return pagination;
	}

	public DictionaryItemPo getDictionaryItem(Long dictionaryItemId) {
		String hql = "from DictionaryItemPo where delStatus=? and id=?";
		DictionaryItemPo dictionaryItemPo = this.findUnique(hql, DelStatusEnum.NORMAL.getValue(), dictionaryItemId);
		return dictionaryItemPo;
	}

	public void editDictionaryItem(DictionaryItemPo dictionaryItemPo) {
		this.update(dictionaryItemPo);
	}
}
