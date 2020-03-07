package com.demo.jpa.entity;

import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

/**
 * Created by Fan at 12:04, on 2020/3/7 .
 */
@Entity
//@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @Column(name = "user_name")
    private String name;

    private String email;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
