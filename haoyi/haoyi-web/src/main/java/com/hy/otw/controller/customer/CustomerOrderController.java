package com.hy.otw.controller.customer;

import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.service.customer.CustomerOrderService;
import com.hy.otw.utils.DownloadUtils;
import com.hy.otw.utils.ExcelUtil;
import com.hy.otw.vo.CustomerOrderVo;
import com.hy.otw.vo.query.CustomerOrderQueryVo;

@RestController
@RequestMapping("/customerOrder")
public class CustomerOrderController {
	
	@Resource private CustomerOrderService customerOrderService;
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public void edit(HttpServletRequest request,HttpServletResponse response, CustomerOrderVo customerOrderVo) throws Exception {
		this.customerOrderService.editCustomerOrder(customerOrderVo);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request,HttpServletResponse response, Long customerOrderId) throws Exception {
		this.customerOrderService.deleteCustomerOrder(customerOrderId);
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Pagination list(HttpServletRequest request,HttpServletResponse response, CustomerOrderQueryVo customerOrderQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.customerOrderService.findCustomerOrderList(customerOrderQueryVo);
		return pagination;
	}
	
	@RequestMapping(value = "/totalAmt", method = RequestMethod.GET)
	public BigDecimal totalAmt(HttpServletRequest request,HttpServletResponse response, CustomerOrderQueryVo customerOrderQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BigDecimal totalAmt = this.customerOrderService.findCustomerOrderTotalAmt(customerOrderQueryVo);
		return totalAmt == null ? new BigDecimal(0.00) : totalAmt;
	}

	@RequestMapping(value = "/loadExcel", method = RequestMethod.POST)
	public void loadExcel(HttpServletRequest request,HttpServletResponse response, CustomerOrderQueryVo customerOrderQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		customerOrderQueryVo.setPage(1);
		customerOrderQueryVo.setRows(Integer.MAX_VALUE);
		Pagination pagination = this.customerOrderService.findCustomerOrderList(customerOrderQueryVo);
		List<CustomerOrderVo> customerOrderVoList =(List<CustomerOrderVo>) pagination.getRows();
		List<JSONObject> customerOrderList = new ArrayList<JSONObject>();
		if(CollectionUtils.isNotEmpty(customerOrderVoList)){
			for (CustomerOrderVo customerOrderVo : customerOrderVoList) {
				JSONObject obj = JSONObject.parseObject(JSONObject.toJSON(customerOrderVo).toString());
				obj.put("settleStatusStr", customerOrderVo.getSettleStatus() == 0 ? "未结算" : "已结算");
				customerOrderList.add(obj);
			}
		}
		OutputStream outputStream = null;
        try {
			String fileName = "客户账单管理";
			String[] fields = { "orderDate", "companyName", "orderNO",	"address", "cabinetModel", "cabinetNumber", "sealNumber", "settlePrice", "otherAmt", "settleStatusStr", "remarks", "createDate" };
			String[] titles = { "订单日期", "公司名称", "订单编号", "订单简址", "柜型", "柜号", "封号", "实结金额", "应结金额", "结算状态", "备注", "创建时间" };
			File file = ExcelUtil.export(null, fileName, fields, titles, customerOrderList, null);
			DownloadUtils.downloadExcel(request, response, file, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
	}
}
