package com.imooc.test;

import javax.sql.DataSource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.druid.pool.DruidDataSource;

public class JDBCRealmTest {
	
	//配置datasource
	private DruidDataSource dataSource = new DruidDataSource();
	{
		dataSource.setUrl("jdbc:mysql://localhost:3306/shiro");
		dataSource.setUsername("root");
		dataSource.setPassword("123");
	}
	
	
	@Test
	//shiro内置jdbcRealm学习
	public void test(){
		//创建jdbcRealm
		JdbcRealm realm = new JdbcRealm();
		//设置datasource 从数据库里取用户,角色和权限的信息
		realm.setDataSource(dataSource);
		//要打开权限开关不然权限验证使不可以的
		realm.setPermissionsLookupEnabled(true);
		
		//用自己的sql语句从数据库中获取用户,角色和权限
		//获取用户验证
		String sql = "select password from test_users where username = ?";
		realm.setAuthenticationQuery(sql);
		//获取角色验证
		String sql2 ="select role_name from test_user_roles where username = ?";
		realm.setUserRolesQuery(sql2);
		//获取权限验证
		String sql3 = "select permission from test_user_permissions where role_name = ?";
		realm.setPermissionsQuery(sql3);
		
		
	
		//构建SecurityManager环境
		DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
		//设置验证的账号
		defaultSecurityManager.setRealm(realm);
			
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
		subject.checkPermission("user:update");
		subject.checkPermission("user:delete");
		
		
		//登出
		subject.logout();
			
		System.out.println("isAuthenticated:"+subject.isAuthenticated());
	}
}
