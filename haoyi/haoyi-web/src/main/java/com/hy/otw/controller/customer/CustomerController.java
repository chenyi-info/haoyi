package com.hy.otw.controller.customer;

import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.service.customer.CustomerService;
import com.hy.otw.utils.DownloadUtils;
import com.hy.otw.utils.ExcelUtil;
import com.hy.otw.vo.CustomerOperatorVo;
import com.hy.otw.vo.CustomerVo;
import com.hy.otw.vo.http.ResponseMsgVo;
import com.hy.otw.vo.query.CustomerQueryVo;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	
@Resource private CustomerService customerService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseMsgVo add(HttpServletRequest request,HttpServletResponse response, CustomerVo customerVo) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.customerService.addCustomer(customerVo);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ResponseMsgVo edit(HttpServletRequest request,HttpServletResponse response, CustomerVo customerVo) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.customerService.editCustomer(customerVo);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseMsgVo delete(HttpServletRequest request,HttpServletResponse response, Long customerId) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.customerService.deleteCustomer(customerId);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Pagination list(HttpServletRequest request,HttpServletResponse response, CustomerQueryVo customerQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.customerService.findCustomerList(customerQueryVo);
		return pagination;
	}
	
	@RequestMapping(value = "/operatorList", method = RequestMethod.GET)
	public Pagination operatorList(HttpServletRequest request,HttpServletResponse response, CustomerQueryVo customerQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.customerService.findCustomerOperatorList(customerQueryVo);
		return pagination;
	}
	
	@RequestMapping(value = "/addOperator", method = RequestMethod.POST)
	public ResponseMsgVo addOperator(HttpServletRequest request,HttpServletResponse response, CustomerOperatorVo customerOperatorVo) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.customerService.addCustomerOperator(customerOperatorVo);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/editOperator", method = RequestMethod.POST)
	public ResponseMsgVo editOperator(HttpServletRequest request,HttpServletResponse response, CustomerOperatorVo customerOperatorVo) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.customerService.editCustomerOperator(customerOperatorVo);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/deleteOperator", method = RequestMethod.POST)
	public ResponseMsgVo deleteOperator(HttpServletRequest request,HttpServletResponse response, Long customerOperatorId) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.customerService.deleteCustomerOperator(customerOperatorId);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}

	@RequestMapping(value = "/loadExcel", method = RequestMethod.POST)
	public void loadExcel(HttpServletRequest request,HttpServletResponse response, CustomerQueryVo customerQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		customerQueryVo.setPage(1);
		customerQueryVo.setRows(Integer.MAX_VALUE);
		Pagination pagination = this.customerService.findCustomerList(customerQueryVo);
		List<CustomerVo> customerVoList =(List<CustomerVo>) pagination.getRows();
		OutputStream outputStream = null;
        try {
			String fileName = "客户管理";
			String[] fields = { "companyName", "contactName",	"contactNumber", "address", "settleInterval","remarks", "createDate" };
			String[] titles = { "公司名称", "联系人姓名", "联系人电话", "公司地址", "结算周期(月)", "备注", "创建时间" };
			File file = ExcelUtil.export(null, fileName, fields, titles, customerVoList, null);
			DownloadUtils.downloadExcel(request, response, file, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
	}
}
