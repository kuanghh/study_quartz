package com.khh.demo1.example6;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * Created by Administrator on 2017/7/22.
 * 这个列子将会展示Quartz如何应对抛出异常的jobs
 */
public class JobExceptionExample {

    public void run() throws Exception{
        //获取一个Scheduler对象
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();


        Date startTime = DateBuilder.nextGivenSecondDate(null, 15);


        JobDetail job = JobBuilder.newJob(BadJob1.class).withIdentity("job1", "group1")
                .usingJobData("denominator","0").build();

        //每10秒重复一次，并且无限重复下去
        SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever()).build();

        Date ft = scheduler.scheduleJob(job, trigger);

        System.out.println(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");

        System.out.println("--------------------------------------------------------------------------------");

        job = JobBuilder.newJob(BadJob2.class).withIdentity("job2","group1").build();

        //每5秒重复一次，并且无限重复下去
        trigger = TriggerBuilder.newTrigger().withIdentity("trigger2", "group1").startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();


        ft = scheduler.scheduleJob(job, trigger);

        System.out.println(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");

        System.out.println("--------------------------------------------------------------------------------");



        scheduler.start();
        System.out.println("------------------start-----------------");

        /**
         * 让Quartz工作30秒
         */
        try{
            Thread.sleep(30L * 1000L);
        }catch (Exception e){

        }

        scheduler.shutdown(true);

        System.out.println("-----------------shutdown---------------");


        SchedulerMetaData metaData = scheduler.getMetaData();
        System.out.println("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
    }

    public static void main(String[] args) throws Exception {
        new JobExceptionExample().run();

    }
}
