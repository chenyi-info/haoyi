package com.hy.otw.controller.dictionary;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.service.dictionary.DictionaryService;
import com.hy.otw.vo.DictionaryItemVo;
import com.hy.otw.vo.DictionaryVo;
import com.hy.otw.vo.http.ResponseMsgVo;
import com.hy.otw.vo.query.DictionaryQueryVo;

@RestController
@RequestMapping("/dictionary")
public class DictionaryController {
	
	@Resource private DictionaryService dictionaryService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseMsgVo add(HttpServletRequest request,HttpServletResponse response, DictionaryVo dictionaryVo) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.dictionaryService.addDictionary(dictionaryVo);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ResponseMsgVo edit(HttpServletRequest request,HttpServletResponse response, DictionaryVo dictionaryVo) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.dictionaryService.editDictionary(dictionaryVo);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseMsgVo delete(HttpServletRequest request,HttpServletResponse response, Long dictionaryId) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.dictionaryService.deleteDictionary(dictionaryId);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Pagination list(HttpServletRequest request,HttpServletResponse response, DictionaryQueryVo dictionaryQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.dictionaryService.findDictionaryList(dictionaryQueryVo);
		return pagination;
	}
	
	@RequestMapping(value = "/itemList", method = RequestMethod.GET)
	public Pagination itemList(HttpServletRequest request,HttpServletResponse response, DictionaryQueryVo dictionaryQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.dictionaryService.findDictionaryItemList(dictionaryQueryVo);
		return pagination;
	}
	
	@RequestMapping(value = "/addItem", method = RequestMethod.POST)
	public ResponseMsgVo addItem(HttpServletRequest request,HttpServletResponse response, DictionaryItemVo dictionaryItemVo) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.dictionaryService.addDictionaryItem(dictionaryItemVo);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/editItem", method = RequestMethod.POST)
	public ResponseMsgVo editItem(HttpServletRequest request,HttpServletResponse response, DictionaryItemVo dictionaryItemVo) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.dictionaryService.editDictionaryItem(dictionaryItemVo);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/deleteItem", method = RequestMethod.POST)
	public ResponseMsgVo deleteItem(HttpServletRequest request,HttpServletResponse response, Long dictionaryItemId) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.dictionaryService.deleteDictionaryItem(dictionaryItemId);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
}
