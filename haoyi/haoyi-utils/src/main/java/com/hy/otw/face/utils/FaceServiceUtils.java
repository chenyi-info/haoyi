package com.hy.otw.face.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.hy.otw.utils.HttpUtils;

/**
 * 获取Face工具类
 * @author chenyi_info@126.com
 *
 */
public class FaceServiceUtils {
	private static ResourceBundle faceURLResource = ResourceBundle.getBundle("face_url");
	private static ResourceBundle faceKeyResource = ResourceBundle.getBundle("face_key");//wechat_key为属性文件名，放在src下，直接用wechat_key即可    
	
	/**
	 * 获取 faceDetect
	 * @return accessToken Map key:access_token,expires_in
	 * @author chenyi_info@126.com
	 * @throws Exception 
	 */
	public static Map faceDetect(String baseImg) throws Exception{
		String appIDkey = faceKeyResource.getString("API_Key_Free");
		String appSecretkey = faceKeyResource.getString("API_Secret_Free");
		String orgBaseImg = baseImg;
		baseImg = baseImg.split(";base64,").length > 1 ? baseImg.split(";base64,")[1] : baseImg;
		int return_landmark = 2;
		String return_attributes = "gender,age,smiling,headpose,facequality,blur,eyestatus,emotion,ethnicity,beauty,mouthstatus,eyegaze,skinstatus";
		String faceUrl = faceURLResource.getString("face_detect");
		Map<String,Object> httpParams = new HashMap<String, Object>();
		httpParams.put("api_key", appIDkey);
		httpParams.put("api_secret", appSecretkey);
		httpParams.put("return_landmark", return_landmark);
		httpParams.put("return_attributes", return_attributes);
		httpParams.put("image_base64", baseImg);
		String resultParams =  HttpUtils.sendPost(faceUrl, httpParams);
		Map map = new HashMap<String, String>();
		map.put("result", resultParams);
		map.put("img", orgBaseImg);
		return map;
	}
	
}