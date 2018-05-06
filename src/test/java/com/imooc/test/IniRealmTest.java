package com.imooc.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class IniRealmTest {

	@Test
	//shiro����iniRealmѧϰ
	public void test(){
			//����iniRealm
			IniRealm iniRealm = new IniRealm("classpath:user.ini");
		
			//����SecurityManager����
			DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
			//������֤���˺�
			defaultSecurityManager.setRealm(iniRealm);
				
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
			subject.checkPermission("user:delete");
			subject.checkPermission("user:update");
			
			
			//�ǳ�
			subject.logout();
				
			System.out.println("isAuthenticated:"+subject.isAuthenticated());
	}
}
