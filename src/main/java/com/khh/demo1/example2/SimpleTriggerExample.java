package com.khh.demo1.example2;

import com.sun.corba.se.impl.orbutil.closure.Future;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

/**
 * Created by FSTMP on 2017/7/20.
 */
//这个示例将演示使用简单触发器的quartz调度功能的所有基本知识。
public class SimpleTriggerExample {

    public void run() throws  Exception{
        //获取Scheduler对象
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        //在未来几秒钟内得到一个“回合”时间…
        Date startTime = DateBuilder.nextGivenSecondDate(null, 15);

        //job1 只会运行一次在 at date/time "ts"
        JobDetail job = JobBuilder.newJob(SimpleJob.class).withIdentity("job1", "group1").build();

        SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startAt(startTime).build();

        //调度它运行!
        Date ft = scheduler.scheduleJob(job,trigger);
        System.out.println(job.getKey() + " will run at : " + ft + " and repeat :" + trigger.getRepeatCount() + " times ,every  " +
                trigger.getRepeatInterval() / 1000 + "secondes");

        System.out.println("-------------------------------------------------------------------------------------------");

        //job2 will only fire once at date/time "ts"
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job2","group1").build();

        trigger = (SimpleTrigger)TriggerBuilder.newTrigger().withIdentity("trigger2","group1").startAt(startTime).build();

        ft = scheduler.scheduleJob(job,trigger);
        System.out.println(job.getKey() + " will run at : " + ft + " and repeat :" + trigger.getRepeatCount() + " times ,every  " +
                trigger.getRepeatInterval() / 1000 + "secondes");

        System.out.println("-------------------------------------------------------------------------------------------");

        //job3 将会跑11次（跑一次和重复11次）;
        //job3 每10秒重复一次
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job3", "group1").build();

        trigger = TriggerBuilder.newTrigger().withIdentity("trigger3", "group1").startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).withRepeatCount(10)).build();

        ft = scheduler.scheduleJob(job, trigger);
        System.out.println(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");

        System.out.println("-------------------------------------------------------------------------------------------");

        // the same job (job3) 将由另一触发器触发
        // 重复两次，每次间隔 是 13 秒。
        trigger = TriggerBuilder.newTrigger().withIdentity("trigger3","group2").startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(13).withRepeatCount(2)).forJob(job).build();

        ft = scheduler.scheduleJob(trigger);
        System.out.println(job.getKey() + " will [also] run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");

        System.out.println("-------------------------------------------------------------------------------------------");

        //job4 将会运行6次，（跑一次，重复5次）
        //每10秒重复一次
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job4","group1").build();

        trigger = TriggerBuilder.newTrigger().withIdentity("trigger4","group1").startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).withRepeatCount(5)).build();

        ft = scheduler.scheduleJob(job,trigger);
        System.out.println(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");

        System.out.println("-------------------------------------------------------------------------------------------");

        //job5 将会运行一次,在未来的5分钟里
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job5", "group1").build();

        trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity("trigger5","group1").
                startAt(DateBuilder.futureDate(5, DateBuilder.IntervalUnit.MINUTE)).build();

        ft = scheduler.scheduleJob(job,trigger);
        System.out.println(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");

        System.out.println("-------------------------------------------------------------------------------------------");

        // job6 将无限期运行，每40秒
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job6", "group1").build();

        trigger = TriggerBuilder.newTrigger().withIdentity("trigger6", "group1").startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(40).repeatForever()).build();

        ft = scheduler.scheduleJob(job, trigger);
        System.out.println(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");

        System.out.println("------- Starting Scheduler ----------------");
        // 所有的作业都添加到调度程序中，但没有一个作业会运行直到 调度器 启动
        scheduler.start();
        System.out.println("------- Started Scheduler ----------------");

        // job7 也可以在调度器start()后被调度...
        // job7 将会运行20次，每5分钟一次
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job7", "group1").build();

        trigger = TriggerBuilder.newTrigger().withIdentity("trigger7", "group1").startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(5).withRepeatCount(20)).build();

        ft = scheduler.scheduleJob(job, trigger);
        System.out.println(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");


        // jobs can be fired directly... (rather than waiting for a trigger)
        //工作可以直接触发…（而不是等待触发）
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job8", "group1").storeDurably().build();

        scheduler.addJob(job, true);

        System.out.println("'Manually' triggering job8...");
        scheduler.triggerJob(JobKey.jobKey("job8", "group1"));

        System.out.println("--------等待30秒----------");
        try {
            // wait 30 seconds to show jobs
            Thread.sleep(30L * 1000L);
            // executing...
        } catch (Exception e) {
            //
        }

        // jobs可以被重新调度...
        // job 7 将立即运行，每秒钟重复10次。
        System.out.println("------- Rescheduling... --------------------");
        trigger = TriggerBuilder.newTrigger().withIdentity("trigger7", "group1").startAt(startTime)
                .withSchedule(simpleSchedule().withIntervalInMinutes(5).withRepeatCount(20)).build();

        ft = scheduler.rescheduleJob(trigger.getKey(), trigger);
        System.out.println("job7 rescheduled to run at: " + ft);

        System.out.println("------- 等待5分钟... ------------");
        try {
            // wait five minutes to show jobs
            Thread.sleep(300L * 1000L);
            // executing...
        } catch (Exception e) {
            //
        }

        System.out.println("------- Shutting Down ---------------------");

        scheduler.shutdown(true);

        System.out.println("------- Shutdown Complete -----------------");

        //显示一些刚刚运行的进度表的统计信息
        SchedulerMetaData metaData = scheduler.getMetaData();
        System.out.println("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
    }

    public static void main(String[] args) throws Exception {
        new SimpleTriggerExample().run();
    }
}
