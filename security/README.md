# Spring Security 
搭建demo,数据库模型的认证与授权

与SpringBoot结合使用，配置文件甚至不用写任何东西，主要的配置都在WebSecurityConfiguration这个类中，
让其继承WebSecurityConfigurerAdapter类，并覆盖configure方法以实现配置

参考的大佬[gist](https://gist.github.com/sunwu51/eac8f64809ad934e9513ee2887980987)
# SpringBoot + Spring Security
## 1 快速开始
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-security</artifactId>
</dependency>
```
直接启动项目，就能在调试信息中看到一个默认生成的密码，用户名是user。如果我们访问资源就会被拦截，有个默认的输入界面/login和一个httpBasic认证页面我们通过这个账号密码可以登陆，登陆后就可以访问资源。
### 1.1 发生了什么
SpringBoot是可以自动配置的，所以当我们写好依赖后，有哪些东西是帮我们配置好的呢？最原始的配置生效在SpringBootWebSecurityConfiguration这个类中，配置的规则在WebSecurityConfigurerAdapter类中继承下来的默认配置。
```java
((HttpSecurity)((HttpSecurity)((AuthorizedUrl)http
	.authorizeRequests()
		.anyRequest()).authenticated()
		.and())
	.formLogin()
		.and())
	.httpBasic();
```
默认配置的意义是对于任意请求都需要验证身份，并支持form表单登录和basic登录。form表单登录的开启会自动将/login作为登录页面并生成一个默认的简陋登录页面(get)和一个应对登录的/login(post)处理，这个处理接受username password _csrf（如果关闭了csrf则不用该参）。  
## 2 概念整理
在安全方面除了基本的登录还有权限的控制，比如管理员用户登录后可以访问的资源和普通用户组肯定是不一样。如果没有登录访问资源会返回401错误，这时候会自动跳转/login页面。而如果普通用户访问管理员才能访问的资源，则会报403错误。  
  
一般我们在设计用户的数据库表的时候就应该至少设计两个表，一个是用户信息表另一个是用户权限表，用户表用来查用户名+密码判断登录是否成功，用户权限表用来在登录成功后查询该用户名的所有权限，只有权限合法才能访问对应的资源。
## 3 接入数据库
快速开始中我们可以通过一个固定的账密登录，这显然只是demo环境，正式环境下用户信息都是放到数据库中的。如何接入数据库呢？这里有两种方案，一是直接用SpringSecurity提供的jdbcAuthentication，二是配置上自己实现UserDetailsService接口的类。方法1适合表结构和默认表结构（下面会说）很相似，可能只是字段名表明差异的情况，方法2则是万能的和灵活的，就是写的东西多点，适用于与默认完全不同的表结构或者甚至不是通过sql存储的情况。  
  
### 3.1 通过jdbcAuthentication整合数据库
```java
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    @Autowired
    DataSource dataSource;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
        ;
    }
}
```
只需要新建一个继承WebSecurityConfigurerAdapter的类，就能覆盖掉自动配置的相关类，并重写上述方法，即可完成jdbc配置。
但是需要注意改数据源需要满足以下表结构：
```
users
     - username
     - password
     - enabled
authorities
     - username
     - authority
```
或者没有Anthonies这个表，而是这三个表则是改个标志位【详见JdbcDaoIml这个类】，关系有点变化
```
groups
     - id  
     - group_name
group_members
     - username 
     - group_id
group_authorities
     - authority 
     - group_id
```
以上为必备字段，可多不可少。如果字段或者表名稍有不同则可以修改相应的查询sql语句来适应，上面的auth也有修改查询语句的方法【详见AuthenticationManagerBuilder】。
### 3.2 自定义UsersDetailsService接口整合数据库
```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService);
}
```
UsersDetailsService是SpringSecurity提供的接口，只要实现这个接口将其传入上述方法即可，这个接口中只有一个返回UserDetails的方法，所以我们第一步要做的是实现UserDetails接口。详细的实现可以看代码，这里不再多说，总之思路是能通过用户名获取用户的密码和权限列表，至于怎么获取的就需要自己写代码实现，我们通过Jdbc查询的方式获取，所以就和方法1到达了一模一样的效果。
## 4 规则配置
上面重写的configure方法参数为AuthenticationManagerBuilder，下面简称auth。是专门配置权限相关的东西的，3中我们就是来配置权限的数据源和认证方式的。WebSecurityConfigurerAdapter这个类中还有另一个重要的configure方法参数为HttpSecurity。这个是干嘛的呢？  
  
当我们配置了认证的信息后，我们还需要配置web上哪些url是不用认证就可以访问的，哪些是只有认证才可以访问的，还有哪些是具有管理员权限才可以访问的。这就是HttpSecurity中需要配置的。【记住这里可以配置的东西很多，但是必须配置的却没有，我们只挑出需要的进行配置即可】
```java
protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                        .anyRequest().authenticated()
                        .and()
                .formLogin()
                        .and()
                .httpBasic();
}
```
默认配置大概如上（我去掉了一些可有可无的强制转换），默认配置中涵盖了非常重要的三个部分，第一是对于请求的认证authorizeRequests，我们可以在这配置需要身份认证的url和需要xx权限的url以及什么都不需要的url
### 4.1 authorizeRequests
```java
.authorizeRequests()//对于请求的过滤，添加是否需要认证、需要角色的URL
	.antMatchers("/mylogin").permitAll()//可以不用登陆访问
	.antMatchers("/admin/**").hasRole("ADMIN")//必须具有ROLE_ADMIN权限
        .anyRequest().authenticated()//需要登陆
```
你可能会疑惑/mylogin他既满足第一条不用登陆的也满足最后一条需要登录的他会是怎么样的呢？答案是按照不用登陆来，antMatchers的是优先级高的或者说更具体的放在前面，anyRequest则总是最后使用。
例如配置/user/\*\*是需要USER的role而/user/login是permitAll则必须把/user/login放在前面，因为他更具体需要更早生效，而如果把/user/\*\*放在前面则会导致/user/login先被拦截。
### 4.2 formLogin/logout
```java
.formLogin()
    .loginPage("/login")        //登录页默认是/login
    .usernameParameter("user")  //username参数默认是username
    .passwordParameter("pwd")   //password参数默认是password
    .successForwardUrl("/index")//登录成功跳转页默认好像是登录前的url
    .failureForwardUrl("/error")//错误页默认是/error
.logout()
    .logoutUrl("/logout")       //退出url默认没有，注意如果开启了csrf还需要上传token字段
    .logoutSuccessUrl("/index") //退出成功后跳转页
```
### 4.3 header
```java
.headers()
    .contentSecurityPolicy("script-src 'self' https://cdn.bootcss.com")//CSP
    .and()
    .frameOptions().sameOrigin()  //X-Frame-Options
    .cacheControl().disable()     //Cache-Control
    .xssProtection().block(true)  //X-XSS-Protection
```
### 4.4 csrf basic
```java
.csrf()       //默认开启的在非get请求中必须上传_csrf字段值为服务端定好的token值    
	.disable()
.httpBasic()  //不写的话默认关闭，开启后可以通过Basic认证身份
	.disable()
```
## 5 注解
一般的规则我们在4中配置，但是有时候url的资源配置非常灵活多变，这样导致配置的内容又多又乱。如果能直接在控制器或其方法上添加注解就好了。@PreAuthorize正是这样的一个强大注解。
注意下面带#的是需要该注解修饰的函数的参数中有该字段，principal则是当前的用户信息，and和&&通用。
```java
 @PreAuthorize("hasRole('ADMIN')")
 @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
 @PreAuthorize("#id<10")                
 @PreAuthorize("principal.username.equals(#username)")
 @PreAuthorize("hasRole('FLY') && #oauth2.hasScope('read')"

```