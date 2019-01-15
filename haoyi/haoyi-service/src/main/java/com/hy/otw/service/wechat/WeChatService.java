//package com.hy.otw.service.wechat;
//
//import java.util.Map;
//
//import javax.annotation.Resource;
//
//import org.apache.commons.collections.MapUtils;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.stereotype.Service;
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.hy.otw.redis.utils.RedisSlave;
//import com.hy.otw.wechat.utils.WeChatServiceUtils;
//import com.hy.otw.wechat.vo.WxConsts;
//
//@Service
//public class WeChatService {
//	
//	@Resource private RedisSlave redisSlave;
//	private WeChatServiceUtils weChatServiceUtils;
//	/**
//	 * 存储微信access token到redis的key
//	 */
//	private final static String OTW_ACCESSTOKEN = "otw-access_token";
//	
//	/**
//	 * 存储微信jsapi_ticket 到redis的key
//	 */
//	private final static String OTW_JSAPITICKET = "otw-jsapi_ticket";
//	
//	/**
//	 * 避免多个服务向微信发送请求,如果 send_accessToken_request key存在就表示已经有服务向微信发送请求
//	 */
//	private final static String SEND_ACCESSTOKEN_REQUEST = "send_accessToken_request";
//	/**
//	 * 避免多个服务向微信发送请求,如果 send_jsapiTicket_request key存在就表示已经有服务向微信发送请求
//	 */
//	private final static String SEND_JSAPITICKET_REQUEST = "send_jsapiTicket_request";
//	
//	private final static int EXPIRES_IN = 60 * 5;
//	
//	/**
//	 * 获取微信菜单
//	 * @throws Exception
//	 * @author chenyi_info@126.com
//	 */
//	public void getWeChantMenu() throws Exception{
//		String accessToken = this.getAccessToken();
//		Map<String, Object> resultMap = weChatServiceUtils.getMenu(accessToken);
//		System.out.println("");
//	}
//	
//	/**
//	 * 创建微信菜单
//	 * @throws Exception
//	 * @author chenyi_info@126.com
//	 */
//	public void createWeChantMenu() throws Exception{
//		String accessToken = this.getAccessToken();
//		JsonArray btnsArr = new JsonArray();
//		JsonObject btnObj = new JsonObject();
//		btnObj.addProperty("type", WxConsts.BUTTON_VIEW);
//		btnObj.addProperty("name", "相机");
//		btnObj.addProperty("url", "https://chenyi.tsh365.cn/view/face.html");
//		btnsArr.add(btnObj);
//		
//		btnObj = new JsonObject();
//		btnObj.addProperty("type", WxConsts.BUTTON_VIEW);
//		btnObj.addProperty("name", "摄像");
//		btnObj.addProperty("url", "https://chenyi.tsh365.cn/view/cam.html");
//		btnsArr.add(btnObj);
//		String bodyData = "{\"button\":"+btnsArr.toString()+"}";
//		Map<String, Object> resultMap = weChatServiceUtils.createMenu(accessToken, bodyData);
//		System.out.println();
//	}
//	
//	/**
//	 * 删除微信菜单
//	 * @throws Exception
//	 * @author chenyi_info@126.com
//	 */
//	public void deleteWeChantMenu() throws Exception{
//		String accessToken = this.getAccessToken();
//		Map<String, Object> resultMap = weChatServiceUtils.deleteMenu(accessToken);
//	}
//	
//	/**
//	 * 获取wxConfig
//	 * @param url
//	 * @return
//	 * @author chenyi@dtds.com.cn
//	 */
//	public Map<String,String> getWxConfig(String url) {
//		String accessToken = this.getAccessToken();
//		String jsApiTicket = this.getJSApiTicket(accessToken);
//		Map<String,String> configMap = this.weChatServiceUtils.getWXConfig(url, jsApiTicket);
//		return configMap;
//	}
//	
//	/**
//	 * 获取微信Token
//	 * @return
//	 * @author chenyi_info@126.com
//	 */
//	public String getAccessToken(){
//		String accessToken = redisSlave.getString(OTW_ACCESSTOKEN);
//		//如果没有access token 或者token 生存时间低于五分钟  并且没有其它服务器向微信发送请求, 那么就获取新的access token
//		if((StringUtils.isBlank(accessToken) || redisSlave.TTL(OTW_ACCESSTOKEN) < EXPIRES_IN) ){
//			redisSlave.set(SEND_ACCESSTOKEN_REQUEST, SEND_ACCESSTOKEN_REQUEST, -1);
//			Map<String, Object> resultMap = weChatServiceUtils.getAccessToken();
//			accessToken = MapUtils.getString(resultMap, "access_token");
//			int expiresIn = MapUtils.getIntValue(resultMap, "expires_in");
//			redisSlave.set(OTW_ACCESSTOKEN, accessToken, expiresIn);
//			redisSlave.delete(SEND_ACCESSTOKEN_REQUEST);
//		}
//		return accessToken;
//	}
//
//	/**
//	 * 获取微信Token
//	 * @return
//	 * @author chenyi_info@126.com
//	 */
//	public String getJSApiTicket(String accessToken){
//		String jsApiTicket = redisSlave.getString(OTW_JSAPITICKET);
//		//如果没有jsapi_ticket 或者jsapi_ticket 生存时间低于五分钟  并且没有其它服务器向微信发送请求, 那么就获取新的jsapi_ticket
//		if((StringUtils.isBlank(jsApiTicket) || redisSlave.TTL(OTW_JSAPITICKET) < EXPIRES_IN) ){
//			redisSlave.set(SEND_JSAPITICKET_REQUEST, SEND_JSAPITICKET_REQUEST, -1);
//			Map<String, Object> resultMap = weChatServiceUtils.getJSApiTicket(accessToken);
//			jsApiTicket = MapUtils.getString(resultMap, "ticket");
//			int expiresIn = MapUtils.getIntValue(resultMap, "expires_in");
//			redisSlave.set(OTW_JSAPITICKET, jsApiTicket, expiresIn);
//			redisSlave.delete(SEND_JSAPITICKET_REQUEST);
//		}
//		return jsApiTicket;
//	}
//
//}
