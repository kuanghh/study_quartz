package com.khh.demo1.example12;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import java.util.Date;

/**
 * Created by FSTMP on 2017/7/24.
 */
public class SimpleJob implements Job{

    public static final String MESSAGE = "msg";

    public SimpleJob(){

    }


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobKey jobKey = context.getJobDetail().getKey();

        String message = (String) context.getJobDetail().getJobDataMap().get(MESSAGE);
        /**
         * 这项工作只是打印出它的工作名称和
         *  正在运行的日期和时间
         */

        System.out.println("SimpleJob: " + jobKey + " executing at " + new Date());
        System.out.println("SimpleJob: msg: " + message);
    }
}
