package com.khh.demo1.example4;

import com.khh.demo1.example3.SimpleJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * Created by FSTMP on 2017/7/20.
 */
public class JobStateExample {

    public void run() throws Exception {
        //获取Scheduler对象
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        // 开始时间是下一个10秒
        Date startTime = DateBuilder.nextGivenSecondDate(null, 10);


        //job1 将会 跑5次（运行1次，重复4次）,每隔10秒 一次
        JobDetail job1 = JobBuilder.newJob(ColorJob.class).withIdentity("job1", "group1").build();

        SimpleTrigger trigger1 = TriggerBuilder.newTrigger().withIdentity("trigger1","group1")
                .startAt(startTime).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).withRepeatCount(4)).build();

        //将初始化参数传递到作业中
        job1.getJobDataMap().put(ColorJob.FAVORITE_COLOR,"Green");
        job1.getJobDataMap().put(ColorJob.EXECUTION_COUNT,1);

        //调度工作去跑
        Date scheduleTime1 = scheduler.scheduleJob(job1,trigger1);
        System.out.println(job1.getKey() + " will run at: " + scheduleTime1 + " and repeat: " + trigger1.getRepeatCount()
                + " times, every " + trigger1.getRepeatInterval() / 1000 + " seconds");

        System.out.println("----------------------------------------------------------------------------------------------------");

        //job2 will also run 5 times, every 10 seconds

        JobDetail job2 = JobBuilder.newJob(ColorJob.class).withIdentity("job2", "group1").build();

        SimpleTrigger trigger2 = TriggerBuilder.newTrigger().withIdentity("trigger2","group1")
                .startAt(startTime).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).withRepeatCount(4)).build();

        //将初始化参数传递到作业中
        job2.getJobDataMap().put(ColorJob.FAVORITE_COLOR,"Yellow");
        job2.getJobDataMap().put(ColorJob.EXECUTION_COUNT,1);

        Date scheduleTime2 = scheduler.scheduleJob(job2,trigger2);
        System.out.println(job1.getKey() + " will run at: " + scheduleTime2 + " and repeat: " + trigger2.getRepeatCount()
                + " times, every " + trigger2.getRepeatInterval() / 1000 + " seconds");

        System.out.println("----------------调度开始--------------------------");
        scheduler.start();
        System.out.println("----------------等60秒----------------------------");
        try {
            // wait five minutes to show jobs
            Thread.sleep(60L * 1000L);
            // executing...
        } catch (Exception e) {
            //
        }
        scheduler.shutdown(true);
        System.out.println("----------------调度结束--------------------------");

        SchedulerMetaData metaData = scheduler.getMetaData();
        System.out.println("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
    }

    public static void main(String[] args) throws Exception {
        new JobStateExample().run();
    }
}
