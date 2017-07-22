package com.khh.demo1.example7;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * Created by Administrator on 2017/7/22.
 */
public class InterruptExample {

    public void run() throws Exception{

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        Date startTime = DateBuilder.nextGivenSecondDate(null, 15);


        JobDetail job = JobBuilder.newJob(DumbInterruptableJob.class).withIdentity("job1", "group1").build();

        //每5秒重复一次，并且无限重复下去
        SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();

        Date ft = scheduler.scheduleJob(job, trigger);
        System.out.println(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");


        //作业开始调度
        scheduler.start();
        System.out.println("----------------------scheduler start-------------------");

        for (int i = 0; i < 50; i++) {
            try{
                Thread.sleep(7000L);
                //告诉scheduler打断我们的工作
                scheduler.interrupt(job.getKey());
            }catch (Exception e){

            }
        }

        scheduler.shutdown(true);
        System.out.println("----------------------scheduler shutdown-------------------");

        SchedulerMetaData metaData = scheduler.getMetaData();
        System.out.println("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");

    }

    public static void main(String[] args) throws Exception{
        new InterruptExample().run();
    }
}
