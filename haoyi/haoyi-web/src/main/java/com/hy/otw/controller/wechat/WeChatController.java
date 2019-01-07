package com.hy.otw.controller.wechat;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.hy.otw.service.wechat.WeChatService;
import com.hy.otw.wechat.utils.SignUtil;

@Controller
public class WeChatController {
	
	@Resource private WeChatService weChatService;
	
	@RequestMapping(value = "/wechat", method = RequestMethod.GET)
	@ResponseBody
	public void add(HttpServletRequest request, HttpServletResponse response,String token) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		// 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。  
        String signature = request.getParameter("signature");  
        // 时间戳  
        String timestamp = request.getParameter("timestamp");  
        // 随机数  
        String nonce = request.getParameter("nonce");  
        // 随机字符串  
        String echostr = request.getParameter("echostr");  
  
        PrintWriter out = null;  
        try {  
            out = response.getWriter();  
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，否则接入失败  
            if (SignUtil.checkSignature(token, signature, timestamp, nonce)) {  
                out.print(echostr);  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            out.close();  
            out = null;  
        }  
	}
	
	@RequestMapping(value = "/wechat/config", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> getWxConfig(HttpServletRequest request, HttpServletResponse response, String url){
		Map<String,String> configMap = new HashMap<String, String>();
        try {
        	configMap = this.weChatService.getWxConfig(url);
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        return configMap;
	}

}
