# Shiro认证,授权,自定义Realm

---

## Shiro认证

- 完整代码

```
private SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();
	
	@Before
	//初始化Realm  相当于验证的账号
	public void addRealm(){
		simpleAccountRealm.addAccount("a", "123");
	}
	
	@Test
	public void test(){
		//构建SecurityManager环境
		DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
		//设置验证的账号
		defaultSecurityManager.setRealm(simpleAccountRealm);
		
		//主体提交认证请求
		SecurityUtils.setSecurityManager(defaultSecurityManager);
		Subject subject = SecurityUtils.getSubject();
		
		//登录的账号
		UsernamePasswordToken token = new UsernamePasswordToken("a","123");
		//登录认证
		subject.login(token);
		System.out.println("isAuthenticated:"+subject.isAuthenticated());
		
		//登出
		subject.logout();
		
		System.out.println("isAuthenticated:"+subject.isAuthenticated());
	}
```

1. 创建SecurityManager
2. 主体提交验证
3. SecurityManager认证
4. Authenticator认证
5. Realm认证

## Shiro授权

1. 创建SecurityManager
2. 主体授权
3. SecurityManager授权
4. Authorizer授权
5. Realm获取角色权限数据


- 先赋予Realm角色
```
simpleAccountRealm.addAccount("root", "123","root");
```
- 也可以赋予多个角色
-
```
simpleAccountRealm.addAccount("root", "123","root","user");
```
- 认证完后就可以进行授权

```
subject.checkRole("root");
```
- 也可以授予多个角色

```
subject.checkRoles("root","user");
```
- 完整代码

```
private SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();
	
	@Before
	//初始化Realm  相当于验证的账号
	public void addRealm(){
		simpleAccountRealm.addAccount("a", "123","root");
	}
	
	@Test
	public void test(){
		//构建SecurityManager环境
		DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
		//设置验证的账号
		defaultSecurityManager.setRealm(simpleAccountRealm);
		
		//主体提交认证请求
		SecurityUtils.setSecurityManager(defaultSecurityManager);
		Subject subject = SecurityUtils.getSubject();
		
		//登录的账号
		UsernamePasswordToken token = new UsernamePasswordToken("a","123");
		//登录认证
		subject.login(token);
		System.out.println("isAuthenticated:"+subject.isAuthenticated());
		
		//授权
		subject.checkRole("root");
		
		
		//登出
		subject.logout();
		
		System.out.println("isAuthenticated:"+subject.isAuthenticated());
	}
```



## shiro realm
### 内置realm


#### iniRealm
- 创建IniRealm对象

```
IniRealm iniRealm = new IniRealm("claspath:user.ini")
```
classspath在maven工程中就是==src/main/resource==目录下

-创建user.ini文件

```
[users]
root=123456,admin
[roles]
admin=user:delete,user:update
```

表示root用户有admin角色

admin角色有user的删除和更新权限

- 将iniRealm赋给SecurityManager

```
defaultSecurityManager.setRealm(iniRealm);
```

- 授权和权限检测

```
//授权
subject.checkRole("admin");
//检测权限
subject.checkPermission("user:delete");
subject.checkPermission("user:update");
```
- 完整代码

```
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
```



#### jdbcRealm

看名字就知道是从数据库中查询用户进行验证的

- 配置datasource
使用阿里的druid

```
//配置datasource
private DruidDataSource dataSource = new DruidDataSource();
{
	dataSource.setUrl("jdbc:mysql://localhost:3306/shiro");
	dataSource.setUsername("root");
	dataSource.setPassword("123");
}
```
- 在数据库中建表
如果不是自己手写sql表的名称与字段要与JdbcRealm类里面的sql语句的表名与字段名相对应

```
CREATE DATABASE shiro;


CREATE TABLE users(
	id INT PRIMARY KEY AUTO_INCREMENT,
	username CHAR(20),
	PASSWORD CHAR(20)
);

CREATE TABLE user_roles(
	role_name VARCHAR(50),
	username VARCHAR(50)
);

CREATE TABLE roles_permissions(
	role_name VARCHAR(50),
	permission VARCHAR(50)
);

```

- 创建jdbcRealm并传入datasource

```
//创建jdbcRealm
JdbcRealm realm = new JdbcRealm();
//设置datasource 从数据库里取用户,角色和权限的信息
realm.setDataSource(dataSource);
```
-==打开权限开关==
```
//要打开权限开关不然角色的权限不能验证
realm.setPermissionsLookupEnabled(true);
```



- 数据库中的用户,角色和权限表与JdbcRealm内置的字段与表面不同就使用自己的sql查询

```
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
```

### 自定义realm
