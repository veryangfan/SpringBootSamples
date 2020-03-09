package com.demo.security.configration;

import com.demo.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by Fan at 21:26, on 2020/3/8 .
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    public BCryptPasswordEncoder passwordEncoder() {
        // 设置默认的加密方式
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //从数据库获取该用户
        MyUserDetails myUserDetails = userRepository.findById(s).get();

        //正常应该是在注册时候存入数据库就是BCryptPasswordEncoder加密的密码，但模拟实验没有注册，我们就取出后在加密
        myUserDetails.setPassword(passwordEncoder().encode(myUserDetails.getPassword()));

        System.out.println("==============" + myUserDetails + "====================");
        //用户不存在则抛出异常
        if(myUserDetails == null){
            throw new UsernameNotFoundException("用户不存在");
        }

        //将数据库形式的roles解析为UserDetails的权限集
        // AuthorityUtils.commaSeparatedStringToAuthorityList()方法
        //是由Spring Security提供的，该方法用于将逗号隔开的权限集字符串切割成可用权限对象列表
        myUserDetails.setAuthorities(
                AuthorityUtils.commaSeparatedStringToAuthorityList(myUserDetails.getRoles()));

        return myUserDetails;
    }
}
