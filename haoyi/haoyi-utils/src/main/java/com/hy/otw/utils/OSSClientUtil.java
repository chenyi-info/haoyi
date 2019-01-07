package com.hy.otw.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;

public class OSSClientUtil {

	private static Logger logger = Logger.getLogger(OSSClientUtil.class);
	/**
	 * 使用端必须配置动态配置客户端
	 */
	private static ResourceBundle ossResource = ResourceBundle.getBundle("oss");
	
	/**
	 * 7W4w72Dfu49QnYSW
	 */
	static String accessKeyId = ossResource.getString("AccessId");
	/**
	 * OlML9l54TAg0EF1CyDj7lA6HQtGCwh
	 */
	static String accessKeySecret = ossResource.getString("AccessKey");
	/**
	 * tshtest
	 */
	static String picBucket = ossResource.getString("PicBucket");
	
	static String desBucket = ossResource.getString("DesBucket");
	
	/**
	 * http://tshtest.zhc365.cn/
	 */
	static String picDomain = ossResource.getString("PicDomain");
	/**
	 * http://oss-cn-shenzhen.aliyuncs.com
	 */
	static String endpoint = ossResource.getString("endpoint");

	// 初始化一个OSSClient
	static OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
	
	

	/**
	 * 接受图片流并保存到OSS
	 * 
	 * @param content
	 *            图片数据流
	 * @param size
	 *            文件件大小
	 * @param picName
	 *            文件名称
	 * @throws IOException
	 */
	public static String putImage(InputStream content,String suffix) throws RuntimeException {
		String desName = "";
		try {
			ObjectMetadata meta = new ObjectMetadata();
			// 必须设置ContentLength
			meta.setContentLength(content.available());
			meta.setContentType("image/jpeg");
			// 上传Object.
		
			desName = UUIDUtil.getUUID()+suffix;
			
			ossClient.putObject(picBucket, desName, content, meta);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("图片保存到OSS时异常", e);
			throw new RuntimeException("图片保存异常");
		} finally {
			if (content != null) {
				try {
					content.close();
				} catch (IOException e) {
					logger.error("保存图片时流关闭异常", e);
				}
			}
		}
		return desName;
	}
	
	
	
	/**
	 * @param content
	 * @param fileName 如：dasdf.pdf
	 * @return filePath
	 * @throws RuntimeException
	 */
	public static String putFile(InputStream content, String fileName) throws RuntimeException {
		String desName = "";
		try {
			ObjectMetadata meta = new ObjectMetadata();
			// 必须设置ContentLength
			meta.setContentLength(content.available());
			String suffix = fileName.substring(fileName.lastIndexOf("."));
			meta.setContentType(contentType(suffix.substring(1)));  
			desName = UUIDUtil.getUUID()+suffix;
			// 上传Object.
			ossClient.putObject(picBucket, desName, content, meta);
		} catch (Exception e) {
			logger.error("文件保存到OSS时异常", e);
			throw new RuntimeException("文件保存异常");
		} finally {
			if (content != null) {
				try {
					content.close();
				} catch (IOException e) {
					logger.error("保存文件流关闭异常", e);
				}
			}
		}
		return picDomain+desName;
	}
	
	
	 /** 
     * Description: 判断OSS服务文件上传时文件的contentType 
     * @Version1.0 
     * @param FilenameExtension 文件后缀 
     * @return String  
     */  
     public static String contentType(String FilenameExtension){  
        if(FilenameExtension.equalsIgnoreCase("bmp")){return "image/bmp";}  
        if(FilenameExtension.equalsIgnoreCase("gif")){return "image/gif";}  
        if(FilenameExtension.equalsIgnoreCase("jpeg")||  
           FilenameExtension.equalsIgnoreCase("jpg")||     
           FilenameExtension.equalsIgnoreCase("png")){return "image/jpeg";}  
        if(FilenameExtension.equalsIgnoreCase("html")){return "text/html";}  
        if(FilenameExtension.equalsIgnoreCase("text")||FilenameExtension.equalsIgnoreCase("txt")){return "text/plain";}  
        if(FilenameExtension.equalsIgnoreCase("vsd")){return "application/vnd.visio";}  
        if(FilenameExtension.equalsIgnoreCase("pdf")){return "application/pdf";}
        if(FilenameExtension.equalsIgnoreCase("zip")){return "application/zip";}
        if(FilenameExtension.equalsIgnoreCase("rar")){return "application/rar";}
        if(FilenameExtension.equalsIgnoreCase("xlsx")||FilenameExtension.equalsIgnoreCase("xls")){return "application/vnd.ms-excel";}
        if(FilenameExtension.equalsIgnoreCase("pptx")||  
            FilenameExtension.equalsIgnoreCase("ppt")){return "application/vnd.ms-powerpoint";}  
        if(FilenameExtension.equalsIgnoreCase("docx")||  
            FilenameExtension.equalsIgnoreCase("doc")){return "application/msword";}  
        if(FilenameExtension.equalsIgnoreCase("xml")){return "text/xml";}  
        return "text/html";  
     }  
	
	public static String getFilePath(String fileName) {
		return picDomain + fileName;
	}

	/**
	 * 将商品描述信息保存到OSS
	 * 
	 * @param content
	 *            文件流
	 * @param desName
	 *            保存名称
	 * @throws IOException
	 */
	public static String putGoodsDes(InputStream content) throws RuntimeException {

		String desName = "";

		try {
			ObjectMetadata meta = new ObjectMetadata();

			// 必须设置ContentLength
			meta.setContentLength(content.available());
			meta.setContentType("text/html");

			desName = "DES_" + UUIDUtil.getUUID();

			ossClient.putObject(desBucket, desName, content, meta);
		} catch (Exception e) {
			logger.error("商品描述保存到OSS时异常", e);
			throw new RuntimeException("商品描述保存异常");
		} finally {
			if (content != null) {
				try {
					content.close();
				} catch (IOException e) {
					logger.error("保存商品描述时流关闭异常", e);
				}
			}
		}
		return desName;
	}

	public static void deleteImage(String name) {
		ossClient.deleteObject(picBucket, name);
	}

	/**
	 * 根据描述路径返回在OSS中的描述内容
	 * 
	 * @param desName
	 * @return
	 * @throws IOException
	 */
	public static String getGoodsDes(String desName) throws RuntimeException {
		InputStream objectContent = null;
		try {
			OSSObject object = ossClient.getObject(desBucket, desName);
			objectContent = object.getObjectContent();
			String result = inputStreamUtil(objectContent);
			return result;
		} catch (Exception e) {
			throw new RuntimeException("获取商品描述时异常");
		} finally {
			if (objectContent != null) {
				try {
					objectContent.close();
				} catch (IOException e) {
					// logger.error("获取商品描述时流关闭异常", e);
				}
			}
		}
	}
	
	public static String getSuffix(String fileName){
		int pos = fileName.lastIndexOf(".");
		if (pos!=-1) {
			return fileName.substring(pos);
		}
		return ".unknow";
	}

	/**
	 * 
	 * @Title: inputStreamUtil 文件流转字符串
	 * @Description: TODO
	 * @param @param
	 *            inputStream
	 * @param @return
	 * @param @throws
	 *            IOException
	 * @return String
	 * @throw
	 */
	public static String inputStreamUtil(InputStream inputStream) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		try {
			while ((i = inputStream.read()) != -1) {
				baos.write(i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return baos.toString();
	}
	
	/**
	 * 
	 * @Title: convertStringToStream 字符串转文件流
	 * @Description: TODO
	 * @param @param content
	 * @return InputStream
	 * @throw
	 */
	public static InputStream convertStringToStream(String content) {
		
		InputStream is = null;
		try {
			is = new ByteArrayInputStream(content.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return is;
		
	}

}
