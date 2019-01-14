package com.hy.otw.controller.vehicle;

import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.service.vehicle.VehicleCareService;
import com.hy.otw.utils.DownloadUtils;
import com.hy.otw.utils.ExcelUtil;
import com.hy.otw.vo.VehicleCareVo;
import com.hy.otw.vo.query.VehicleCareQueryVo;

@RestController
@RequestMapping("/vehicleCare")
public class VehicleCareController {
	
	@Resource private VehicleCareService vehicleCareService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(HttpServletRequest request,HttpServletResponse response, VehicleCareVo vehicleCareVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		this.vehicleCareService.addVehicleCare(vehicleCareVo);
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public void edit(HttpServletRequest request,HttpServletResponse response, VehicleCareVo vehicleCareVo) throws Exception {
		this.vehicleCareService.editVehicleCare(vehicleCareVo);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request,HttpServletResponse response, Long vehicleCareId) throws Exception {
		this.vehicleCareService.deleteVehicleCare(vehicleCareId);
	}
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Pagination list(HttpServletRequest request,HttpServletResponse response, VehicleCareQueryVo vehicleCareQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.vehicleCareService.findVehicleCareList(vehicleCareQueryVo);
		return pagination;
	}
	
	@RequestMapping(value = "/totalAmt", method = RequestMethod.GET)
	public BigDecimal totalAmt(HttpServletRequest request,HttpServletResponse response, VehicleCareQueryVo vehicleCareQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BigDecimal totalAmt = this.vehicleCareService.findVehicleCareTotalAmt(vehicleCareQueryVo);
		return totalAmt;
	}
	
	@RequestMapping(value = "/loadExcel", method = RequestMethod.POST)
	public void loadExcel(HttpServletRequest request,HttpServletResponse response, VehicleCareQueryVo vehicleCareQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		vehicleCareQueryVo.setPage(1);
		vehicleCareQueryVo.setRows(Integer.MAX_VALUE);
		Pagination pagination = this.vehicleCareService.findVehicleCareList(vehicleCareQueryVo);
		List<VehicleCareVo> vehicleCareVoList = (List<VehicleCareVo>) pagination.getRows();
		if(CollectionUtils.isNotEmpty(vehicleCareVoList)){
			for (VehicleCareVo vehicleCareVo : vehicleCareVoList) {
				if(vehicleCareVo.getCareInterval() == null || vehicleCareVo.getCareInterval() == 0){
					Long days = (Long) ((new Date().getTime() - vehicleCareVo.getCareDate().getTime()) / (1000*3600*24));
					vehicleCareVo.setCareInterval(days);
				}
			}
		}
		OutputStream outputStream = null;
        try {
			String fileName = "车辆保养管理";
			String[] fields = { "careDate", "plateNumber",	"ownerName", "contactNumber", "itemName", "price", "careInterval", "remarks", "createDate" };
			String[] titles = { "保养日期", "车牌号", "司机姓名", "联系电话", "保养项目", "保养金额", "保养间距(天)", "备注", "创建时间" };
			File file = ExcelUtil.export(null, fileName, fields, titles, vehicleCareVoList, null);
			DownloadUtils.downloadExcel(request, response, file, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
	}

}
