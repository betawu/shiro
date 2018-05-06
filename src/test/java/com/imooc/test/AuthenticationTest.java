package com.imooc.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

public class AuthenticationTest {

	private SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();
	
	@Before
	public void add(){
		simpleAccountRealm.addAccount("root", "123","root","user");
	}
	
	@Test
	//shiro认证
	public void test(){
		DefaultSecurityManager securityManager = new DefaultSecurityManager();
		securityManager.setRealm(simpleAccountRealm);
		
		SecurityUtils.setSecurityManager(securityManager);
		Subject subject = SecurityUtils.getSubject();
		
		UsernamePasswordToken token = new UsernamePasswordToken("root","123");
		subject.login(token);
		System.out.println(subject.isAuthenticated());
		
		subject.logout();
		
		System.out.println(subject.isAuthenticated());
	}
	
	@Test
	//shiro授权
	public void test2(){
		DefaultSecurityManager securityManager = new DefaultSecurityManager();
		securityManager.setRealm(simpleAccountRealm);
		
		SecurityUtils.setSecurityManager(securityManager);
		Subject subject = SecurityUtils.getSubject();
		
		UsernamePasswordToken token = new UsernamePasswordToken("root","123");
		subject.login(token);
		System.out.println(subject.isAuthenticated());
		
		//授权
		subject.checkRoles("root","user");
		
		
		subject.logout();
		
		System.out.println(subject.isAuthenticated());
	}
	
}
