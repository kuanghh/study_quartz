package com.khh.demo1.example12;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by FSTMP on 2017/7/24.
 *
 * 客户端，让服务端调度
 */
public class RemoteClientExample {

    public void run() throws Exception{
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = null;
        try{
            System.getProperties().setProperty("org.quartz.properties","demo1/example12/client.properties");
            scheduler = schedulerFactory.getScheduler();
        }catch (Exception e){
            e.printStackTrace();
        }

        JobDetail job = JobBuilder.newJob(SimpleJob.class).withIdentity("remotelyAddedJob", "default").build();

        JobDataMap jobDataMap = job.getJobDataMap();

        jobDataMap.put("msg","Your remotely added job has executed!");

        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("remotelyAddedTrigger", "default").forJob(job.getKey())
                .withSchedule(CronScheduleBuilder.cronSchedule("/5 * * ? * *")).build();

        scheduler.scheduleJob(job,trigger);

        System.out.println("Remote job scheduled.");
    }

    public static void main(String[] args) throws Exception{
        RemoteClientExample example = new RemoteClientExample();
        example.run();
    }
}
