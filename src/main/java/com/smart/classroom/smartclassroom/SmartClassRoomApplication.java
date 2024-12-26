package com.smart.classroom.smartclassroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SmartClassRoomApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartClassRoomApplication.class, args);
    }

}
