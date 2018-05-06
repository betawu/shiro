package com.imooc.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class IniRealmTest {

	@Test
	//shiro内置iniRealm学习
	public void test(){
			//创建iniRealm
			IniRealm iniRealm = new IniRealm("classpath:user.ini");
		
			//构建SecurityManager环境
			DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
			//设置验证的账号
			defaultSecurityManager.setRealm(iniRealm);
				
			//主体提交认证请求
			SecurityUtils.setSecurityManager(defaultSecurityManager);
			Subject subject = SecurityUtils.getSubject();
				
			//登录的账号
			UsernamePasswordToken token = new UsernamePasswordToken("root","123456");
			//登录验证
			subject.login(token);
			System.out.println("isAuthenticated:"+subject.isAuthenticated());
				
			//授权
			subject.checkRole("admin");
			//检测权限
			subject.checkPermission("user:delete");
			subject.checkPermission("user:update");
			
			
			//登出
			subject.logout();
				
			System.out.println("isAuthenticated:"+subject.isAuthenticated());
	}
}
