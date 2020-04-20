package com.hy.otw.controller.order;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.service.order.OrderService;
import com.hy.otw.utils.DownloadUtils;
import com.hy.otw.utils.ExcelUtil;
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
	
	@RequestMapping(value = "/uploadWord", method = RequestMethod.POST)
	public OrderVo uploadWord(HttpServletRequest request,HttpServletResponse response, @RequestParam("file") MultipartFile file) {
		OrderVo orderVo = new OrderVo();
		if (!file.isEmpty()) { 
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
			String regEx="[^0-9]";
			Pattern p = Pattern.compile(regEx);  
			InputStream ins = null;
			try {
				ins = file.getInputStream();
				if (file.getOriginalFilename().toLowerCase().endsWith("doc")) {
					POIFSFileSystem pfs = new POIFSFileSystem(ins);//得到word文档的信息
					HWPFDocument hwpf = new HWPFDocument(pfs);
					Range range = hwpf.getRange();//得到文档的读取范围
					TableIterator it = new TableIterator(range);
					int tIndex = 1;
					while (it.hasNext()) { 
						Table tb = (Table) it.next();
						TableRow tr = tb.getRow(0);
						TableCell td = tr.getCell(1);//取得单元格
						String cellText = cellText(td);
						if(tIndex==1){ //第一个表格
							orderVo.setOrderNO(cellText);
							
							tr = tb.getRow(1);
							td = tr.getCell(1);//取得单元格
							cellText = cellText(td);
							orderVo.setOrderDate(sdf.parse(cellText));
							
							tr = tb.getRow(2);
							td = tr.getCell(1);//取得单元格
							cellText = cellText(td);
							orderVo.setCabinetModel(cellText);
						}else{ //第二个表格
							tr = tb.getRow(0);
							td = tr.getCell(1);//取得单元格
							cellText = cellText(td);
							orderVo.setDetailAddress(cellText);
							
							tr = tb.getRow(1);
							td = tr.getCell(1);//取得单元格
							cellText = cellText(td);
							orderVo.setCustomerPrice(new BigDecimal(cellText));
							
							tr = tb.getRow(2);
							td = tr.getCell(1);//取得单元格
							cellText = cellText(td);
							Matcher m = p.matcher(cellText); 
		                    orderVo.setWeighed(new BigDecimal(m.replaceAll("").trim()));
							
							tr = tb.getRow(4);
							td = tr.getCell(1);//取得单元格
							cellText = cellText(td);
							orderVo.setDemand(cellText.trim());
							
							tr = tb.getRow(5);
							td = tr.getCell(1);//取得单元格
							cellText = cellText(td);
							orderVo.setRemarks(cellText.trim());
							
						}
						tIndex++;
					}
					
				} else if(file.getOriginalFilename().toLowerCase().endsWith("docx")){
					 XWPFDocument xwpf  =new XWPFDocument(ins);//得到word文档的信息
					 Iterator<XWPFTable> it = xwpf.getTablesIterator();//得到word中的表格 
					 //第一个表格
					 XWPFTable table = it.next();    
					 List<XWPFTableRow> rows=table.getRows();   
					 XWPFTableRow  row = rows.get(0);
					 XWPFTableCell cell = row.getTableCells().get(1);
					 orderVo.setOrderNO(cell.getText().trim());
					 row = rows.get(1);
					 cell = row.getTableCells().get(1);
					 orderVo.setOrderDate(sdf.parse(cell.getText().trim()));
					 row = rows.get(2);
					 //第二个表格
					 table = it.next(); 
					 rows=table.getRows();
					 cell = row.getTableCells().get(1);
					 orderVo.setCabinetModel(cell.getText().trim());
					 row = rows.get(0);
					 cell = row.getTableCells().get(1);
					 orderVo.setDetailAddress(cell.getText().trim());
					 row = rows.get(1);//运费
					 cell = row.getTableCells().get(1);
					 orderVo.setCustomerPrice(new BigDecimal(cell.getText().trim()));
					 row = rows.get(2);//重量
					 cell = row.getTableCells().get(1);
					 Matcher m = p.matcher(cell.getText().trim()); 
					 orderVo.setWeighed(new BigDecimal(m.replaceAll("").trim()));
					 row = rows.get(4);
					 cell = row.getTableCells().get(1);
					 orderVo.setDemand(cell.getText().trim());
					 row = rows.get(5);
					 cell = row.getTableCells().get(1);
					 orderVo.setRemarks(cell.getText().trim());
					
				}else{
					throw new Exception("文件格式错误");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				//close(ins);
			}
			
		}
		return orderVo;
	}
	
	private static String cellText(TableCell td){
		String s = "";
		 for(int k=0;k<td.numParagraphs();k++){
			 Paragraph para =td.getParagraph(k);
			 s += para.text();
		 }
		//去除后面的特殊符号
		if(null != s && !"".equals(s)){
			s = s.substring(0, s.length()-1);
		}
		return s.trim();
	}
	
	/**
	 * 关闭输入流
	 *
	 * @param is
	 */
	private static void close(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Pagination list(HttpServletRequest request,HttpServletResponse response, OrderQueryVo orderQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.orderService.findOrderList(orderQueryVo);
		return pagination;
	}
	
	@RequestMapping(value = "/loadExcel", method = RequestMethod.POST)
	public void loadExcel(HttpServletRequest request,HttpServletResponse response, OrderQueryVo orderQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		orderQueryVo.setPage(1);
		orderQueryVo.setRows(Integer.MAX_VALUE);
		Pagination pagination = this.orderService.findOrderList(orderQueryVo);
		List<OrderVo> orderVoList = (List<OrderVo>) pagination.getRows();
		List<JSONObject> orderList = new ArrayList<JSONObject>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(CollectionUtils.isNotEmpty(orderVoList)){
			for (OrderVo orderVo : orderVoList) {
				JSONObject obj = JSONObject.parseObject(JSONObject.toJSON(orderVo).toString());
				obj.put("rowNum", orderList.size() + 1 );
				obj.put("orderDateStr", sdf.format(orderVo.getOrderDate()));
				obj.put("createDateStr", sdf.format(orderVo.getCreateDate()));
				obj.put("orderStatusStr", orderVo.getOrderStatus() == 0 ? "正常" : "已取消");
				if(StringUtils.isNotBlank(orderVo.getCabinetRecipientAddr())){
					obj.put("addStr", orderVo.getCabinetRecipientAddr()+ "/" + (StringUtils.isNotBlank(orderVo.getCabinetReturnAddr()) ? orderVo.getCabinetReturnAddr() : ""));
				}
				orderList.add(obj);
			}
		}
		OutputStream outputStream = null;
        try {
			String fileName = "订单管理";
			String[] fields = {"rowNum", "orderDateStr", "orderNO",	 "cabinetModel", "addStr",  "address", "weighed", "demand", "cabinetNumber", "sealNumber",  "plateNumber", "ownerName", "contactNumber", "driverPrice", "companyName", "operatorName", "orderStatusStr", "remarks", "createDateStr" };
			String[] titles = { "序号","订单日期", "订单编号", "柜型", "提还柜", "订单简址", "重量(T)",  "订单要求", "柜号", "封号", "车牌号", "司机姓名", "联系电话",  "划价金额",  "客户公司名称", "操作人", "订单状态", "备注", "创建时间" };
			File file = ExcelUtil.export(null, fileName, fields, titles, orderList, null);
			DownloadUtils.downloadExcel(request, response, file, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
	}

}
