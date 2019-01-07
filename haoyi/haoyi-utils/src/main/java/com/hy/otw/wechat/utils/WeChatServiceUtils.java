package com.hy.otw.wechat.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;

import com.google.gson.Gson;
import com.hy.otw.utils.HttpUtils;
import com.hy.otw.utils.UUIDUtil;

/**
 * 获取微信key工具类
 * @author chenyi_info@126.com
 *
 */
public class WeChatServiceUtils {
	private static ResourceBundle weChatURLResource = ResourceBundle.getBundle("wechat_url");
	private static ResourceBundle weChatKeyResource = ResourceBundle.getBundle("wechat_key");//wechat_key为属性文件名，放在src下，直接用wechat_key即可    
	
	/**
	 * 获取微信 accessToken
	 * @return accessToken Map key:access_token,expires_in
	 * @author chenyi_info@126.com
	 */
	public static Map getAccessToken(){
		String appIDkey = weChatKeyResource.getString("AppID");
		String appSecretkey = weChatKeyResource.getString("AppSecret");
		String sendURL = MessageFormat.format(weChatURLResource.getString("access_token"), appIDkey, appSecretkey);
		String resultInfo = HttpUtils.getContent(sendURL);
		return stringToMap(resultInfo);
	}
	
	/**
	 * 获取微信菜单
	 * @param accessToken
	 * @return
	 * @throws Exception
	 * @author chenyi_info@126.com
	 */
	public static Map getMenu(String accessToken) throws Exception{
		String sendURL = MessageFormat.format(weChatURLResource.getString("menu_get"), accessToken);
		String resultInfo = HttpUtils.getContent(sendURL);
		return stringToMap(resultInfo);
	}
	
	/**
	 * 删除微信菜单
	 * @param accessToken
	 * @return
	 * @throws Exception
	 * @author chenyi_info@126.com
	 */
	public static Map deleteMenu(String accessToken) throws Exception{
		String sendURL = MessageFormat.format(weChatURLResource.getString("menu_delete"), accessToken);
		String resultInfo = HttpUtils.getContent(sendURL);
		return stringToMap(resultInfo);
	}
	
	/**
	 * 创建菜单
	 * @param accessToken accessToken
	 * @param map 创建菜单的参数,参数格式见微信api:https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141013
	 * @return {"errcode":0,"errmsg":"ok"}
	 * @throws Exception
	 * @author chenyi_info@126.com
	 */
	public static Map createMenu(String accessToken, String bodyData) throws Exception{
		String sendURL = MessageFormat.format(weChatURLResource.getString("menu_create"), accessToken);
		String resultInfo = HttpUtils.sendPostBody(sendURL, bodyData, ContentType.APPLICATION_JSON);
		return stringToMap(resultInfo);
	}
	
	/**
	 * 根据URL获取微信配置信息
	 * @param url 当前页面
	 * @param jsApiTicket jsapi_ticket
	 * @return signature/appId/timestamp/nonceStr
	 * @author chenyi_info@126.com
	 */
	public static Map getWXConfig(String url, String jsApiTicket){
		Map<String,String> configMap = new HashMap<String, String>();
		if(StringUtils.isBlank(url)){
			return configMap;
		}
		String appIDkey = weChatKeyResource.getString("AppID");
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);//时间戳  
		String nonceStr = UUIDUtil.getUUID();//随机字符串
		String decript = MessageFormat.format("jsapi_ticket={0}&noncestr={1}&timestamp={2}&url={3}", jsApiTicket, nonceStr, timestamp, url);
		String signature = SHA1(decript);
		configMap.put("appId", appIDkey);
		configMap.put("timestamp", timestamp);
		configMap.put("nonceStr", nonceStr);
		configMap.put("signature", signature);
		return configMap;
	}
	
	/**
	 * 获取微信JSApiTicket
	 * @param accessToken accessToken
	 * @return
	 * @author chenyi_info@126.com
	 */
	public static Map getJSApiTicket(String accessToken){
		String sendURL =  MessageFormat.format(weChatURLResource.getString("get_ticket"), accessToken,"jsapi");
		String resultInfo = HttpUtils.getContent(sendURL);
		return stringToMap(resultInfo);
	}
	
	/**
	 * 将微信返回的String结果组装成Map返回出去
	 * @param resultInfo
	 * @return
	 * @author chenyi@dtds.com.cn
	 */
	public static Map stringToMap(String resultInfo){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap =  new Gson().fromJson(resultInfo, resultMap.getClass());
		return resultMap;
	}
	
	/**
	 * SHA1 加密
	 * @param decript 排序字符
	 * @return
	 * @author chenyi_info@126.com
	 */
	public static String SHA1(String decript) {  
	    try {  
	        MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");  
	        digest.update(decript.getBytes());  
	        byte messageDigest[] = digest.digest();  
	        // Create Hex String  
	        StringBuffer hexString = new StringBuffer();  
	        // 字节数组转换为 十六进制 数  
	            for (int i = 0; i < messageDigest.length; i++) {  
	                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);  
	                if (shaHex.length() < 2) {  
	                    hexString.append(0);  
	                }  
	                hexString.append(shaHex);  
	            }  
	            return hexString.toString();  
	   
	        } catch (NoSuchAlgorithmException e) {  
	            e.printStackTrace();  
	        }  
	        return "";  
	}  
}