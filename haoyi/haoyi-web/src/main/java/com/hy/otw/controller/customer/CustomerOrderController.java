package com.hy.otw.controller.customer;

import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.service.customer.CustomerOrderService;
import com.hy.otw.service.customer.OrderOtherAmtService;
import com.hy.otw.utils.DownloadUtils;
import com.hy.otw.utils.ExcelUtil;
import com.hy.otw.vo.CustomerOrderVo;
import com.hy.otw.vo.OrderOtherAmtVo;
import com.hy.otw.vo.query.CustomerOrderQueryVo;

@RestController
@RequestMapping("/customerOrder")
public class CustomerOrderController {
	
	@Resource private CustomerOrderService customerOrderService;
	
	@Resource private OrderOtherAmtService orderOtherAmtService;
	
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
	
	@RequestMapping(value = "/batchSettles", method = RequestMethod.POST)
	public void batchSettles(HttpServletRequest request,HttpServletResponse response, Long[] customerOrderIds) throws Exception {
		List<Long> customerOrderIdList = Arrays.asList(customerOrderIds);
		this.customerOrderService.batchSettles(customerOrderIdList);
	}

	@RequestMapping(value = "/batchLock", method = RequestMethod.POST)
	public void batchLock(HttpServletRequest request,HttpServletResponse response, Long[] customerOrderIds) throws Exception {
		List<Long> customerOrderIdList = Arrays.asList(customerOrderIds);
		this.customerOrderService.batchLockOrUnLock(customerOrderIdList, 2);
	}
	
	@RequestMapping(value = "/batchUNLock", method = RequestMethod.POST)
	public void batchUNLock(HttpServletRequest request,HttpServletResponse response, Long[] customerOrderIds) throws Exception {
		List<Long> customerOrderIdList = Arrays.asList(customerOrderIds);
		this.customerOrderService.batchLockOrUnLock(customerOrderIdList, 0);
	}
	
	@RequestMapping(value = "/loadExcel", method = RequestMethod.POST)
	public void loadExcel(HttpServletRequest request,HttpServletResponse response, CustomerOrderQueryVo customerOrderQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		customerOrderQueryVo.setPage(1);
		customerOrderQueryVo.setRows(Integer.MAX_VALUE);
		Pagination pagination = this.customerOrderService.findCustomerOrderList(customerOrderQueryVo);
		List<CustomerOrderVo> customerOrderVoList =(List<CustomerOrderVo>) pagination.getRows();
		List<JSONObject> customerOrderList = new ArrayList<JSONObject>();
		List<Long> orderIdlist = null;
		if(CollectionUtils.isNotEmpty(customerOrderVoList)){
			orderIdlist = Lists.transform(customerOrderVoList, new Function<CustomerOrderVo, Long>() {
				 @Override
				 public Long apply( CustomerOrderVo customerOrderVo ) {
					 return customerOrderVo.getOrderId();
				 }
			});
			List<OrderOtherAmtVo> orderOtherAmtVoList = orderOtherAmtService.findOrderOtherAmtList(orderIdlist, 2);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for (CustomerOrderVo customerOrderVo : customerOrderVoList) {
				if(customerOrderVo.getSettlePrice() == null){
					customerOrderVo.setSettlePrice(BigDecimal.ZERO);
				}
				if(customerOrderVo.getOtherAmt() == null){
					customerOrderVo.setOtherAmt(BigDecimal.ZERO);
				}
				if(customerOrderVo.getCustomerPrice() == null){
					customerOrderVo.setCustomerPrice(BigDecimal.ZERO);
				}
				JSONObject obj = JSONObject.parseObject(JSONObject.toJSON(customerOrderVo).toString());
				obj.put("orderDateStr", sdf.format(customerOrderVo.getOrderDate()));
				obj.put("createDateStr", sdf.format(customerOrderVo.getCreateDate()));
				obj.put("settleStatusStr", customerOrderVo.getSettleStatus() == 0 ? "未结算" : customerOrderVo.getSettleStatus() == 1 ? "已结算" : "锁定");
				obj.put("settleItems", orderOtherAmtService.getItemsText(orderOtherAmtVoList, customerOrderVo.getOrderId(), 0));
				obj.put("unsettleItems", orderOtherAmtService.getItemsText(orderOtherAmtVoList, customerOrderVo.getOrderId(), 1));
				String total = "";
				total += StringUtils.isNotBlank(obj.getString("settleItems")) ? obj.getString("settleItems") : "";
				total += StringUtils.isNotBlank(obj.getString("unsettleItems")) ? obj.getString("unsettleItems") : "";
				obj.put("totalSettleItems", total);
				obj.put("totalSettle", customerOrderVo.getOtherAmt().subtract(customerOrderVo.getSettlePrice()).add(customerOrderVo.getCustomerPrice()));
				customerOrderList.add(obj);
			}
		}
		OutputStream outputStream = null;
        try {
			String fileName = "客户账单管理";
			String[] fields = { "orderDateStr", "companyName", "orderNO",	"address", "cabinetModel", "cabinetNumber", "sealNumber", "customerPrice",  "otherAmt", "settleItems", "totalSettle","totalSettleItems", "settleStatusStr", "remarks", "createDateStr" };
			String[] titles = { "订单日期", "公司名称", "订单编号", "订单简址", "柜型", "柜号", "封号", "订单金额", "应结算杂费金额", "已结杂费金额","应结总额","杂费明细", "结算状态", "备注", "创建时间" };
			File file = ExcelUtil.export(null, fileName, fields, titles, customerOrderList, null);
			DownloadUtils.downloadExcel(request, response, file, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
	}
}
