package com.hy.otw.service.auth;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hy.otw.service.auth.UserInfoService;
import com.hy.otw.vo.UserInfoVo;

public class AuthRealmService extends AuthorizingRealm {
	
	@Autowired private UserInfoService userInfoService;
	
    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userName = (String) principalCollection.getPrimaryPrincipal();
        List<String> permissionList=new ArrayList<String>();
        permissionList.add("user:add");
        permissionList.add("user:delete");
        if (userName.equals("zhou")) {
            permissionList.add("user:query");
        }
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addStringPermissions(permissionList);
        authorizationInfo.addRole("list");
        return authorizationInfo;
    }
    
    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
    	//令牌——基于用户名和密码的令牌    
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;    
        //令牌中可以取出用户名  
        String accountName = token.getUsername();  
        //让shiro框架去验证账号密码  
        if(StringUtils.isNotBlank(accountName)){  
        	UserInfoVo userInfo = null;
        	try {
				userInfo = userInfoService.getUserInfoByAccount(accountName);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(userInfo == null || StringUtils.isBlank(userInfo.getUserAccount())){
				return null;
			}
			if(userInfo.getUserAccount().equals(accountName)){
				//取出盐并编码
	            ByteSource salt = ByteSource.Util.bytes(userInfo.getSalt());
	            return new SimpleAuthenticationInfo(accountName, userInfo.getPassword(),salt, getName());
			}else{
				throw new AuthenticationException();
			}
        	
        }  
          
        return null;  
    }


}
