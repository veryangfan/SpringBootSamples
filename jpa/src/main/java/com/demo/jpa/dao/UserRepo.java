package com.demo.jpa.dao;

import com.demo.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Fan at 12:39, on 2020/3/7 .
 */
public interface UserRepo extends JpaRepository<User,Integer> {

    User findByName(String name);

    @Query(value = "select * from user",nativeQuery = true)
    List<User> findUsers();

}

