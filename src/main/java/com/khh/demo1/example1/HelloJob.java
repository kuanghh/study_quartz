package com.khh.demo1.example1;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * This is just a simple job that says "Hello" to the world.
 * Created by FSTMP on 2017/7/20.
 */
//Quartz调度的任务需要实现 Job类
public class HelloJob implements Job{

    public HelloJob(){

    }

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("hello World !" + new Date());
    }
}
