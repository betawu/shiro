package com.imooc.Customer;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

public class CustomerRealm extends AuthorizingRealm{

	{
		//初始化自定义realm的名称
		super.setName("customer");
		
	}
	
	
	//授权方法
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//获取授权的用户
		String username = (String) principals.getPrimaryPrincipal();
		
		//根据用户从数据库或者缓存中获取角色
		Set<String> roles = getRoles(username);
		//根据用户从数据库或者缓存中获取权限
		Set<String> permissions = Permissions(username);
		
		//将角色和权限封装到SimpleAuthorizationInfo对象返回
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setRoles(roles);
		info.setStringPermissions(permissions);
		
		return info;
	}

	private Set<String> Permissions(String username) {
		Set<String> s = new HashSet<String>();
		s.add("user:delete");
		s.add("user:update");
		return s;
	}

	private Set<String> getRoles(String username) {
		Set<String> s = new HashSet<String>();
		s.add("admin");
		return s;
	}

	//认证方法
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//获取主体的用户名
		String username = (String) token.getPrincipal();
		
		//从数据库中获取验证信息
		String password = getPasswordByUsername(username);
		
		//进行验证
		if(password==null) {
			return null;
		}
		
		//创建返回对象
		//将待验证的信息 用户名 密码  和 自定义 realm的名称进行赋值
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo("root",password,"customer");
		//设置秘密啊盐
		info.setCredentialsSalt(ByteSource.Util.bytes("yan"));
		return info;
	}

	
	//省略数据库查询步骤
	private String getPasswordByUsername(String username) {
		return "f80e9178cd46af07822b438c0d8d8e31";
	}

	public static void main(String[] args) {
		//123的md5加密并加盐结果
		Md5Hash md5Hash = new Md5Hash("123","yan");
		System.out.println(md5Hash.toString());
		//f80e9178cd46af07822b438c0d8d8e31
	}
	
	
}
