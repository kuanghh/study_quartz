package com.khh.demo2.service;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by FSTMP on 2017/7/24.
 */
public class Demo {

    public static void main(String[] args) {

//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("demo2/spring-quartz.xml");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("demo2/spring-quartz-anno.xml");

    }
}
