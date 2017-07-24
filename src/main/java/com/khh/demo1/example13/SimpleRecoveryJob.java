package com.khh.demo1.example13;

import org.quartz.*;

import java.util.Date;

/**
 * Created by FSTMP on 2017/7/24.
 */
public class SimpleRecoveryJob implements Job {

    private static final String COUNT = "count";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobKey jobKey = context.getJobDetail().getKey();

        if(context.isRecovering()){
            System.out.println("SimpleRecoveryJob: " + jobKey + " RECOVERING at " + new Date());
        }else{
            System.out.println("SimpleRecoveryJob: " + jobKey + " starting  at " + new Date());
        }

        //增加工作时间
        long delay = 10 * 1000L;
        try{
            Thread.sleep(delay);
        }catch (Exception e){
            //
        }

        JobDataMap data = context.getJobDetail().getJobDataMap();
        int count;
        if(data.containsKey(COUNT)){
            count = data.getInt(COUNT);
        }else{
            count = 0;
        }

        count++;
        data.put(COUNT,count);
        System.out.println("SimpleRecoveryJob: " + jobKey + " done at " + new Date() + "\n Execution #" + count);
    }
}
