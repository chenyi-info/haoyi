package com.hy.otw.controller.driver;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.service.driver.DriverOrderService;
import com.hy.otw.utils.DownloadUtils;
import com.hy.otw.utils.ExcelUtil;
import com.hy.otw.vo.DriverOrderVo;
import com.hy.otw.vo.query.DriverOrderQueryVo;

@RestController
@RequestMapping("/driverOrder")
public class DriverOrderController {
	
	@Resource private DriverOrderService driverOrderService;
	
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public void edit(HttpServletRequest request,HttpServletResponse response, DriverOrderVo driverOrderVo) throws Exception {
		this.driverOrderService.editDriverOrder(driverOrderVo);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request,HttpServletResponse response, Long driverOrderId) throws Exception {
		this.driverOrderService.deleteDriverOrder(driverOrderId);
	}
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Pagination list(HttpServletRequest request,HttpServletResponse response, DriverOrderQueryVo driverOrderQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.driverOrderService.findDriverOrderList(driverOrderQueryVo);
		return pagination;
	}
	
	@RequestMapping(value = "/totalAmt", method = RequestMethod.GET)
	public BigDecimal totalAmt(HttpServletRequest request,HttpServletResponse response, DriverOrderQueryVo driverOrderQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BigDecimal totalAmt = this.driverOrderService.findDriverOrderTotalAmt(driverOrderQueryVo);
		return totalAmt == null ? new BigDecimal(0.00) : totalAmt;
	}
	
	@RequestMapping(value = "/batchSettles", method = RequestMethod.POST)
	public void batchSettles(HttpServletRequest request,HttpServletResponse response, Long[] driverOrderIds) throws Exception {
		List<Long> driverOrderIdList = Arrays.asList(driverOrderIds);
		this.driverOrderService.batchSettles(driverOrderIdList);
	}
	
	@RequestMapping(value = "/loadExcel", method = RequestMethod.POST)
	public void loadExcel(HttpServletRequest request,HttpServletResponse response, DriverOrderQueryVo driverOrderQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		driverOrderQueryVo.setPage(1);
		driverOrderQueryVo.setRows(Integer.MAX_VALUE);
		Pagination pagination = this.driverOrderService.findDriverOrderList(driverOrderQueryVo);
		List<DriverOrderVo> driverOrderVoList =(List<DriverOrderVo>) pagination.getRows();
		List<JSONObject> driverOrderList = new ArrayList<JSONObject>();
		if(CollectionUtils.isNotEmpty(driverOrderVoList)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for (DriverOrderVo driverOrderVo : driverOrderVoList) {
				JSONObject obj = JSONObject.parseObject(JSONObject.toJSON(driverOrderVo).toString());
				obj.put("orderDateStr", sdf.format(driverOrderVo.getOrderDate()));
				obj.put("createDateStr", sdf.format(driverOrderVo.getCreateDate()));
				obj.put("settleStatusStr", driverOrderVo.getSettleStatus() == 0 ? "未结算" : "已结算");
				driverOrderList.add(obj);
			}
		}
		OutputStream outputStream = null;
        try {
			String fileName = "车辆结算管理";
			String[] fields = { "orderDateStr", "plateNumber", "ownerName", "contactNumber","orderNO",	"address", "cabinetModel", "cabinetNumber", "sealNumber", "driverPrice", "settlePrice", "otherAmt", "settleStatusStr", "remarks", "createDateStr" };
			String[] titles = { "订单日期", "车牌号", "司机姓名", "联系电话", "订单编号", "订单简址", "柜型", "柜号", "封号", "划价", "实结金额", "应结金额", "结算状态", "备注", "创建时间" };
			File file = ExcelUtil.export(null, fileName, fields, titles, driverOrderList, null);
			DownloadUtils.downloadExcel(request, response, file, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
	}

}
