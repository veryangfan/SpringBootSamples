package com.demo.security.repository;

import com.demo.security.configration.MyUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Fan at 21:22, on 2020/3/8 .
 */
public interface UserRepository extends JpaRepository<MyUserDetails,String> {

}
