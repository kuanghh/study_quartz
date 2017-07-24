package com.khh.demo2.bean;

import org.springframework.stereotype.Component;

/**
 * Created by FSTMP on 2017/7/24.
 */
@Component("myBean")
public class MyBean {

    public void printMessage(){
        System.out.println("I am MyBean. I am called by MethodInvokingJobDetailFactoryBean using SimpleTriggerFactoryBean");
    }
}
