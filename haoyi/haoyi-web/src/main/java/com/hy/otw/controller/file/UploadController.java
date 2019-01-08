package com.hy.otw.controller.file;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hy.otw.utils.OSSClientUtil;


@Controller
@RequestMapping("/upload")
public class UploadController{	
	private static final String FILENAME_STR= "fileName";
	
	
	
	/**
	 * 适用于webuploader
	 * @param file
	 * @param request
	 * @return
	 * @throws FileNotFoundException 
	 */
	@RequestMapping(value = "/uploadImg.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> uploadImg(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) throws FileNotFoundException{
		Map<Object, Object> map = new HashMap<Object, Object>();		
		if(file.isEmpty()){
			throw new FileNotFoundException("上传文件为空");
		}
		String fileName = "";
		String suffix = OSSClientUtil.getSuffix(file.getOriginalFilename());
		InputStream is=null;
		try {
			is = file.getInputStream();
			fileName = OSSClientUtil.putImage(is,suffix);
			if(is!=null){
				is.close();
			}
		} catch (Exception e) {
			throw new FileNotFoundException("上传文件失败");
		}
		map.put(FILENAME_STR, fileName);
		map.put("path", OSSClientUtil.getFilePath(fileName));
		return map;
	}
	
}
