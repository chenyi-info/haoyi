package com.hy.otw.controller.sys;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hy.otw.hibernate.utils.Pagination;
  

@RestController
@RequestMapping("/mysql")
public class MySQLDatabaseBackupController {  
	private static final String FILE_PATH = "/data/mysqlBackUp/";
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Pagination list(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = new Pagination();
		List<JSONObject> fileList = new ArrayList<JSONObject>();
		File file = new File(FILE_PATH);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                return pagination;
            } else {
                for (File file2 : files) {
                    if (!file2.isDirectory()) {
                        JSONObject fileObj = new JSONObject();
                        fileObj.put("fileName", file2.getName());
                        fileObj.put("filePath", file2.getPath());
                        fileObj.put("fileSize", Math.ceil(file2.length()/1024));
                        fileObj.put("fileTime", file2.lastModified());
                        fileList.add(fileObj);
                    } 
                }
            }
        }
        pagination.setRows(fileList);
        pagination.setTotal(fileList.size());
		return pagination;
	}
	
	@RequestMapping(value = "/download", method = RequestMethod.POST)
	public void download(String fileName, HttpServletResponse response) {
        try {
        	String path = FILE_PATH+fileName;
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
	
	@RequestMapping(value = "/backup", method = RequestMethod.POST)
    public static boolean exportDatabaseTool() throws InterruptedException, IOException { 
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd-HHmmss");
		String hostIP = "127.0.0.1";
		String userName = "";
		String password = "";
		String fileName = ""+sdf.format(new Date())+".sql"; 
		String databaseName="";
		// 创建文件保存的路径
		File saveFile = new File(FILE_PATH);
		if (!saveFile.exists()) {// 如果目录不存在
			saveFile.mkdirs();// 创建文件夹
		}
		String savePath = FILE_PATH;
		if (!FILE_PATH.endsWith(File.separator)) {
			savePath = savePath + File.separator;
		}
		PrintWriter printWriter = null;
		BufferedReader bufferedReader = null;
		try {
			printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(savePath + fileName), "utf8"));
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("mysqldump").append(" --opt").append(" -h").append(hostIP);
			stringBuilder.append(" --user=").append(userName).append(" --password=").append(password)
					.append(" --lock-all-tables=true");
			stringBuilder.append(" --result-file=").append(savePath + fileName).append(" --default-character-set=utf8 ")
					.append(databaseName);
			Process process = Runtime.getRuntime().exec(stringBuilder.toString());
			InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "utf8");
			bufferedReader = new BufferedReader(inputStreamReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				printWriter.println(line);
			}
			printWriter.flush();
			if (process.waitFor() == 0) {// 0 表示线程正常终止。
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (printWriter != null) {
					printWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}