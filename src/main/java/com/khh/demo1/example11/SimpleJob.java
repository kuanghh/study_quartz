package com.khh.demo1.example11;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import java.util.Date;

/**
 * Created by FSTMP on 2017/7/24.
 *
 */
public class SimpleJob implements Job {

    // job parameter
    public static final String DELAY_TIME = "delay time";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobKey jobKey = context.getJobDetail().getKey();

        System.out.println("Executing job: " + jobKey + " executing at " + new Date());

        long delayTime = context.getJobDetail().getJobDataMap().getLong(DELAY_TIME);

        try{
            Thread.sleep(delayTime);
        }catch (Exception e){
            //
        }

        System.out.println("Finished Executing job: " + jobKey + " at " + new Date());
    }
}
