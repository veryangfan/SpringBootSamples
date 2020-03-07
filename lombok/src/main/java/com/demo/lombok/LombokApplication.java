package com.demo.lombok;

import lombok.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sun.tools.jar.CommandLine;

@SpringBootApplication
public class LombokApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(LombokApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        User user1 = new User();
        user1.setId(1);
        user1.setName("liming");
        user1.setAge(18);

//        User user2 = new User(2,"hanmeimei",17);
        User user2 = new User(1,"liming",18);

        System.out.println(user1);
        System.out.println(user2);
        System.out.println(user1 == user2);
        System.out.println(user1.equals(user2));

    }

    @Data
//    @Getter
//    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class User{
        private Integer id;
        private String name;
        private Integer age;
    }
}
