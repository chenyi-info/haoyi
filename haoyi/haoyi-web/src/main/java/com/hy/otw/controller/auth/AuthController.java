package com.hy.otw.controller.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hy.otw.vo.UserInfoVo;
import com.hy.otw.vo.http.ResponseMsgVo;

/**
 * 鉴权登录
 * <p>Title: AuthController</p>  
 * <p>Description: </p>  
 * @author chenyi_info@126.com  
 * @date 2019年1月4日
 */
@RestController
@RequestMapping("/auth")
public class AuthController{
	
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseMsgVo login(HttpServletRequest request,HttpServletResponse response, String userAccount, String userPassWord) {
    	ResponseMsgVo msgVo = new ResponseMsgVo();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userAccount, userPassWord);
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            msgVo.setMsg("用户名或密码错误");
            msgVo.setStatus(500);
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            msgVo.setMsg("用户名或密码错误");
            msgVo.setStatus(500);
        }
        return msgVo;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	SecurityUtils.getSubject().logout();
    }
    
    @RequestMapping(value = "/userInfo", method = RequestMethod.POST)
    public UserInfoVo userInfo(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	UserInfoVo userInfo =  (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
    	userInfo.setPassword(null);
    	userInfo.setSalt(null);
    	return userInfo;
    }
    
}
