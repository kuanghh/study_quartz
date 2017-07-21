package com.khh.demo1.example5;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * Created by FSTMP on 2017/7/21.
 *  misfire -- 指的是 错过了触发时间
 */
public class MisFireExample {

    public void run() throws Exception {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        Date startTime = DateBuilder.nextGivenSecondDate(null, 15);

        //job1 触发器设定每3秒钟触发一次，但是工作需要10秒钟的执行时间(延迟了10秒)
        JobDetail job = JobBuilder.newJob(StatefulDumbJob.class).withIdentity("statefulJob1", "group1")
                .usingJobData(StatefulDumbJob.EXECUTIONS_DELAY, 10000L).build();

        SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).repeatForever()).build();

        Date ft = scheduler.scheduleJob(job, trigger);
        System.out.println(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");

        //job2 触发器设定每3秒钟触发一次，但是工作需要10秒钟的执行时间
        //当检测到丢失触发时，不会立即触发，而是忽略本次安排到下一个预定时间去触发
        job = JobBuilder.newJob(StatefulDumbJob.class).withIdentity("statefulJob2", "group1")
                .usingJobData(StatefulDumbJob.EXECUTIONS_DELAY, 10000L).build();

        trigger = TriggerBuilder.newTrigger().withIdentity("trigger2", "group1").startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(3)
                        .repeatForever()
                        //设置失败指令:表示当job因为job执行时间过长 而 错过触发器时 job执行完后立即再次执行job
                        .withMisfireHandlingInstructionNowWithExistingCount()).build();


        ft = scheduler.scheduleJob(job, trigger);
        System.out.println(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");

        scheduler.start();
        System.out.println("-----------------------------------调度开始----------------------------");

        try {
            Thread.sleep(600L * 1000L);
        } catch (Exception e) {

        }
        scheduler.shutdown(true);
        System.out.println("-----------------------------------调度结束----------------------------");

        SchedulerMetaData metaData = scheduler.getMetaData();
        System.out.println("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
    }

    public static void main(String[] args) throws Exception {
        new MisFireExample().run();
    }
}