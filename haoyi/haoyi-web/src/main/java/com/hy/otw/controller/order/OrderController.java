package com.hy.otw.controller.order;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.service.order.OrderService;
import com.hy.otw.vo.OrderVo;
import com.hy.otw.vo.http.ResponseMsgVo;
import com.hy.otw.vo.query.OrderQueryVo;

@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Resource private OrderService orderService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseMsgVo add(HttpServletRequest request,HttpServletResponse response, OrderVo orderVo, Boolean hasConfirm) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			msg = this.orderService.addOrder(orderVo, hasConfirm);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public ResponseMsgVo edit(HttpServletRequest request,HttpServletResponse response, OrderVo orderVo, Boolean hasConfirm){
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			msg = this.orderService.editOrder(orderVo, hasConfirm);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseMsgVo delete(HttpServletRequest request,HttpServletResponse response, Long orderId){
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.orderService.deleteOrder(orderId);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Pagination list(HttpServletRequest request,HttpServletResponse response, OrderQueryVo orderQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.orderService.findOrderList(orderQueryVo);
		return pagination;
	}

}
