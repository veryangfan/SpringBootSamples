package com.demo.security.configration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

/**
 * Created by Fan at 20:53, on 2020/3/8 .
 */
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //直接通过Jdbc的方式进行用户auth配置，需要遵守一定的表结构，可以查看domain下两个类的结构
//        auth.jdbcAuthentication().dataSource(dataSource);

        //这种方法则比较灵活，使用自定义数据模型（参考user.sql），注入自定义的userDetailsService即可
        //在SpringSecurity5.0以后，不允许存入数据库明文密码，需要有加密方式
        //然后利用Builder即可创建出用户
        auth.userDetailsService(myUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/admin/api/**").hasRole("ADMIN")
                .antMatchers("/user/api/**").hasRole("USER")
                .anyRequest().permitAll()
                .and()
            .formLogin()
                .and()
            .httpBasic()
                .and()
            .csrf().disable();
    }
}
