package com.khh.demo1.example3;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import java.util.Date;

/**
 * Created by FSTMP on 2017/7/20.
 */
//这只是一个简单的工作
public class SimpleJob implements Job{

    public SimpleJob(){

    }
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        /**
         * 这项工作只是打印出它的工作名称和
         *  正在运行的日期和时间
         */
        JobKey key = jobExecutionContext.getJobDetail().getKey();
        System.out.println("SimpleJob says: " + key + "executing at: " + new Date());
    }
}
