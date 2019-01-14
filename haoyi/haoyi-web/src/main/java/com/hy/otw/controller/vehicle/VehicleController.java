package com.hy.otw.controller.vehicle;

import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.service.vehicle.VehicleService;
import com.hy.otw.utils.DownloadUtils;
import com.hy.otw.utils.ExcelUtil;
import com.hy.otw.vo.VehicleVo;
import com.hy.otw.vo.http.ResponseMsgVo;
import com.hy.otw.vo.query.VehicleQueryVo;

/**
 * 车辆管理
 * <p>Title: VehicleController</p>  
 * <p>Description: </p>  
 * @author chenyi@dtds.com.cn  
 * @date 2019年1月10日
 */
@RestController
@RequestMapping("/vehicle")
public class VehicleController {
	
	@Resource private VehicleService vehicleService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseMsgVo add(HttpServletRequest request,HttpServletResponse response, VehicleVo vehicleVo) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.vehicleService.addVehicle(vehicleVo);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ResponseMsgVo edit(HttpServletRequest request,HttpServletResponse response, VehicleVo vehicleVo) throws Exception {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.vehicleService.editVehicle(vehicleVo);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseMsgVo delete(HttpServletRequest request,HttpServletResponse response, Long vehicleId) throws Exception {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.vehicleService.deleteVehicle(vehicleId);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Pagination list(HttpServletRequest request,HttpServletResponse response, VehicleQueryVo vehicleQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.vehicleService.findVehicleList(vehicleQueryVo);
		return pagination;
	}
	
	@RequestMapping(value = "/loadExcel", method = RequestMethod.POST)
	public void loadExcel(HttpServletRequest request,HttpServletResponse response, VehicleQueryVo vehicleQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		vehicleQueryVo.setPage(1);
		vehicleQueryVo.setRows(Integer.MAX_VALUE);
		Pagination pagination = this.vehicleService.findVehicleList(vehicleQueryVo);
		List<VehicleVo> vehicelVoList =(List<VehicleVo>) pagination.getRows();
		OutputStream outputStream = null;
        try {
			String fileName = "车辆管理";
			String[] fields = { "plateNumber", "ownerName",	"contactNumber", "vehicleType", "remarks", "createDate" };
			String[] titles = { "车牌号", "司机姓名", "联系电话", "车辆类型", "备注", "创建时间" };
			File file = ExcelUtil.export(null, fileName, fields, titles, vehicelVoList, null);
			DownloadUtils.downloadExcel(request, response, file, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
	}

}
