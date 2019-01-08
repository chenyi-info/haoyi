package com.hy.otw.dao.dictionary;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.hibernate.utils.HibernateDao;
import com.hy.otw.hibernate.utils.Page;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.DictionaryPo;
import com.hy.otw.vo.query.DictionaryQueryVo;

@Repository
public class DictionaryDao extends HibernateDao<DictionaryPo, Long>{

	public void addDictionary(DictionaryPo dictionaryPo) {
		this.save(dictionaryPo);
	}

	public Pagination findDictionaryList(DictionaryQueryVo dictionaryQueryVo) {
		StringBuffer hql = new StringBuffer("from DictionaryPo where delStatus=?");
		List<Object> param = new ArrayList<Object>();
		param.add(DelStatusEnum.NORMAL.getValue());
		Page<DictionaryPo> page = new Page<DictionaryPo>();
		page.setPageNo(dictionaryQueryVo.getPage());
		page.setPageSize(dictionaryQueryVo.getRows());
		if(StringUtils.isNotBlank(dictionaryQueryVo.getDictionaryName())){
			hql.append(" and name like '%").append(dictionaryQueryVo.getDictionaryName()).append("%'");
		}
		hql.append(" order by updateDate desc");
		
		Pagination pagination = this.findPagination(page, hql.toString(), param.toArray());
		return pagination;
	}

	public DictionaryPo getDictionary(Long dictionaryId) {
		String hql = "from DictionaryPo where delStatus=? and id=?";
		DictionaryPo dictionaryPo = this.findUnique(hql, DelStatusEnum.NORMAL.getValue(), dictionaryId);
		return dictionaryPo;
	}

	public void editDictionary(DictionaryPo dictionaryPo) {
		this.update(dictionaryPo);
	}

	public DictionaryPo getDictionaryByCode(String code) {
		String hql = "from DictionaryPo where delStatus=? and code=?";
		DictionaryPo dictionaryPo = this.findUnique(hql, DelStatusEnum.NORMAL.getValue(), code);
		return dictionaryPo;
	}
}
