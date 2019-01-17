package com.hy.otw.service.dictionary;

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
import com.hy.otw.dao.dictionary.DictionaryDao;
import com.hy.otw.dao.dictionary.DictionaryItemDao;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.DictionaryItemPo;
import com.hy.otw.po.DictionaryPo;
import com.hy.otw.vo.DictionaryItemVo;
import com.hy.otw.vo.DictionaryVo;
import com.hy.otw.vo.UserInfoVo;
import com.hy.otw.vo.query.DictionaryQueryVo;

@Service
public class DictionaryService {
	
	@Resource private DictionaryDao dictionaryDao;
	@Resource private DictionaryItemDao dictionaryItemDao;
	
	public void addDictionary(DictionaryVo dictionaryVo) throws Exception {
		DictionaryPo dictionaryPo = this.dictionaryDao.getDictionaryByCode(dictionaryVo.getCode());
		if(dictionaryPo != null){
			throw new Exception("字典编码不能重复");
		}
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		Date date = new Date();
		dictionaryPo = new DictionaryPo();
		dictionaryVo.setCreateBy(loginUser.getId());
		dictionaryVo.setCreateDate(date);
		dictionaryVo.setUpdateBy(loginUser.getId());
		dictionaryVo.setUpdateDate(date);
		dictionaryVo.setDelStatus(DelStatusEnum.NORMAL.getValue());
		PropertyUtils.copyProperties(dictionaryPo, dictionaryVo);
		this.dictionaryDao.addDictionary(dictionaryPo);
	}

	public Pagination findDictionaryList(DictionaryQueryVo dictionaryQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.dictionaryDao.findDictionaryList(dictionaryQueryVo);
		List<DictionaryPo> dictionaryPoList = (List<DictionaryPo>) pagination.getRows();
		List<DictionaryVo> dictionaryVoList = new ArrayList<DictionaryVo>();
		if(CollectionUtils.isNotEmpty(dictionaryPoList)){
			for (DictionaryPo dictionaryPo : dictionaryPoList) {
				DictionaryVo dictionaryVo = new DictionaryVo();
				PropertyUtils.copyProperties(dictionaryVo, dictionaryPo);
				dictionaryVoList.add(dictionaryVo);
			}
		}
		pagination.setRows(dictionaryVoList);
		return pagination;
	}

	public void editDictionary(DictionaryVo dictionaryVo) throws Exception {
		
		DictionaryPo dictionaryPo = this.dictionaryDao.getDictionaryByCode(dictionaryVo.getCode());
		if(dictionaryPo != null && dictionaryPo.getId() != dictionaryVo.getId()){
			throw new Exception("字典编码不能重复");
		}
		if(dictionaryPo == null){
		 dictionaryPo = dictionaryDao.getDictionary(dictionaryVo.getId());
		}
		if(dictionaryPo == null){
			throw new Exception("未找到该条信息");
		}
		Date date = new Date();
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		dictionaryVo.setCreateBy(dictionaryPo.getCreateBy());
		dictionaryVo.setCreateDate(dictionaryPo.getCreateDate());
		dictionaryVo.setUpdateBy(loginUser.getId());
		dictionaryVo.setUpdateDate(date);
		dictionaryVo.setDelStatus(dictionaryPo.getDelStatus());
		PropertyUtils.copyProperties(dictionaryPo, dictionaryVo);
		this.dictionaryDao.editDictionary(dictionaryPo);
	}

	public void deleteDictionary(Long dictionaryId) throws Exception {
		DictionaryPo dictionaryPo = dictionaryDao.getDictionary(dictionaryId);
		if(dictionaryPo == null){
			throw new Exception("未找到该条信息");
		}
		Date date = new Date();
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		dictionaryPo.setDelStatus(DelStatusEnum.HIDE.getValue());
		dictionaryPo.setUpdateBy(loginUser.getId());
		dictionaryPo.setUpdateDate(date);
		this.dictionaryDao.editDictionary(dictionaryPo);
	}

	public Pagination findDictionaryItemList(DictionaryQueryVo dictionaryQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.dictionaryItemDao.findDictionaryItemList(dictionaryQueryVo);
		List<DictionaryItemPo> dictionaryItemPoList = (List<DictionaryItemPo>) pagination.getRows();
		List<DictionaryItemVo> dictionaryItemVoList = new ArrayList<DictionaryItemVo>();
		if(CollectionUtils.isNotEmpty(dictionaryItemPoList)){
			for (DictionaryItemPo dictionaryItemPo : dictionaryItemPoList) {
				DictionaryItemVo dictionaryItemVo = new DictionaryItemVo();
				PropertyUtils.copyProperties(dictionaryItemVo, dictionaryItemPo);
				dictionaryItemVoList.add(dictionaryItemVo);
			}
		}
		pagination.setRows(dictionaryItemVoList);
		return pagination;
	}

	public void addDictionaryItem(DictionaryItemVo dictionaryItemVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Date date = new Date();
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		DictionaryItemPo dictionaryItemPo = new DictionaryItemPo();
		dictionaryItemVo.setCreateBy(loginUser.getId());
		dictionaryItemVo.setCreateDate(date);
		dictionaryItemVo.setUpdateBy(loginUser.getId());
		dictionaryItemVo.setUpdateDate(date);
		dictionaryItemVo.setDelStatus(DelStatusEnum.NORMAL.getValue());
		PropertyUtils.copyProperties(dictionaryItemPo, dictionaryItemVo);
		this.dictionaryItemDao.addDictionaryItem(dictionaryItemPo);
	}
	
	public void editDictionaryItem(DictionaryItemVo dictionaryItemVo) throws Exception {
		DictionaryItemPo dictionaryItemPo = dictionaryItemDao.getDictionaryItem(dictionaryItemVo.getId());
		if(dictionaryItemPo == null){
			throw new Exception("未找到该条信息");
		}
		Date date = new Date();
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		dictionaryItemVo.setCreateBy(dictionaryItemPo.getCreateBy());
		dictionaryItemVo.setCreateDate(dictionaryItemPo.getCreateDate());
		dictionaryItemVo.setUpdateBy(loginUser.getId());
		dictionaryItemVo.setUpdateDate(date);
		dictionaryItemVo.setDelStatus(dictionaryItemPo.getDelStatus());
		PropertyUtils.copyProperties(dictionaryItemPo, dictionaryItemVo);
		this.dictionaryItemDao.editDictionaryItem(dictionaryItemPo);
	}

	public void deleteDictionaryItem(Long dictionaryItemId) throws Exception {
		DictionaryItemPo dictionaryItemPo = dictionaryItemDao.getDictionaryItem(dictionaryItemId);
		if(dictionaryItemPo == null){
			throw new Exception("未找到该条信息");
		}
		Date date = new Date();
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		dictionaryItemPo.setDelStatus(DelStatusEnum.HIDE.getValue());
		dictionaryItemPo.setUpdateBy(loginUser.getId());
		dictionaryItemPo.setUpdateDate(date);
		this.dictionaryItemDao.editDictionaryItem(dictionaryItemPo);
	}
}
