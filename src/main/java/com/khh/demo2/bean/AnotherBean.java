package com.khh.demo2.bean;

import org.springframework.stereotype.Component;

/**
 * Created by FSTMP on 2017/7/24.
 */
@Component("anotherBean")
public class AnotherBean {
    public void printAnotherMessage(){
        System.out.println("I am AnotherBean. I am called by Quartz jobBean using CronTriggerFactoryBean");
    }
}
