package com.demo.jpa.Controller;

import com.alibaba.fastjson.JSONObject;
import com.demo.jpa.dao.UserRepo;
import com.demo.jpa.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Created by Fan at 12:49, on 2020/3/7 .
 */
@RestController
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/user")
    public User getUserById(@RequestParam("id") Integer id){
        Optional<User> user = userRepo.findById(id);
        return user.isPresent() ? user.get() : null;
    }

    @GetMapping("/user/name")
    public User getUserByName(@RequestParam("name") String name){
        return userRepo.findByName(name);
    }

    @GetMapping("/users")
    public List<User> getUsers(){
        return userRepo.findUsers();
    }

    @PostMapping("/user")
    public User addUser(@RequestBody JSONObject json){
        User user = new User();
        user.setName(json.getString("name"));
        user.setEmail(json.getString("email"));

        //save()
        return userRepo.save(user);
    }
}
