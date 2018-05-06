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
	
	//����datasource
	private DruidDataSource dataSource = new DruidDataSource();
	{
		dataSource.setUrl("jdbc:mysql://localhost:3306/shiro");
		dataSource.setUsername("root");
		dataSource.setPassword("123");
	}
	
	
	@Test
	//shiro����jdbcRealmѧϰ
	public void test(){
		//����jdbcRealm
		JdbcRealm realm = new JdbcRealm();
		//����datasource �����ݿ���ȡ�û�,��ɫ��Ȩ�޵���Ϣ
		realm.setDataSource(dataSource);
		//Ҫ��Ȩ�޿��ز�ȻȨ����֤ʹ�����Ե�
		realm.setPermissionsLookupEnabled(true);
		
		//���Լ���sql�������ݿ��л�ȡ�û�,��ɫ��Ȩ��
		//��ȡ�û���֤
		String sql = "select password from test_users where username = ?";
		realm.setAuthenticationQuery(sql);
		//��ȡ��ɫ��֤
		String sql2 ="select role_name from test_user_roles where username = ?";
		realm.setUserRolesQuery(sql2);
		//��ȡȨ����֤
		String sql3 = "select permission from test_user_permissions where role_name = ?";
		realm.setPermissionsQuery(sql3);
		
		
	
		//����SecurityManager����
		DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
		//������֤���˺�
		defaultSecurityManager.setRealm(realm);
			
		//�����ύ��֤����
		SecurityUtils.setSecurityManager(defaultSecurityManager);
		Subject subject = SecurityUtils.getSubject();
			
		//��¼���˺�
		UsernamePasswordToken token = new UsernamePasswordToken("root","123456");
		//��¼��֤
		subject.login(token);
		System.out.println("isAuthenticated:"+subject.isAuthenticated());
			
		//��Ȩ
		subject.checkRole("admin");
		//���Ȩ��
		subject.checkPermission("user:update");
		subject.checkPermission("user:delete");
		
		
		//�ǳ�
		subject.logout();
			
		System.out.println("isAuthenticated:"+subject.isAuthenticated());
	}
}
