package com.khh.demo1.example3;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * Created by FSTMP on 2017/7/20.
 */
// 这个类会展示 所有  Quartz中的CronTrigger的基本功能
public class CronTriggerExample {

    public void run() throws SchedulerException {
        //获取Scheduler对象
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        //匹配的格式分别是 秒 分 时 日 月 周几
        //job1 将会每20秒运行一次
        JobDetail job = JobBuilder.newJob(SimpleJob.class).withIdentity("job1", "group1").build();

        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/20 * * * * ?")).build();

        Date ft = scheduler.scheduleJob(job,trigger);
        System.out.println(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
                + trigger.getCronExpression());

        System.out.println("---------------------------------------------------------------------------------------");


        //job2每两分钟跑一次（每分钟的第15秒，触发）
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job2","group1").build();

        trigger = TriggerBuilder.newTrigger().withIdentity("trigger2", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("15 0/2 * * * ? ")).build();

        ft = scheduler.scheduleJob(job,trigger);
        System.out.println(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
                + trigger.getCronExpression());

        System.out.println("---------------------------------------------------------------------------------------");

        //job3 在每天早上的8点到下午的5点，每两分钟运行一次
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job3","group1").build();

        trigger = TriggerBuilder.newTrigger().withIdentity("trigger3", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/2 8-17 * * ?")).build();

        ft = scheduler.scheduleJob(job,trigger);
        System.out.println(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
                + trigger.getCronExpression());

        System.out.println("---------------------------------------------------------------------------------------");

        //job4 在每天的下午5点到每天晚上11点，每3分钟运行一次
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job4","group1").build();

        trigger = TriggerBuilder.newTrigger().withIdentity("trigger4", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/3 17-23 * * ?")).build();

        ft = scheduler.scheduleJob(job,trigger);
        System.out.println(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
                + trigger.getCronExpression());

        System.out.println("---------------------------------------------------------------------------------------");

        //job5 每个月的1-20号的每天下午2点，每隔1分钟调用一次
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job5","group1").build();

        trigger = TriggerBuilder.newTrigger().withIdentity("trigger5", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 14 1,20 * ?")).build();

        ft = scheduler.scheduleJob(job,trigger);
        System.out.println(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
                + trigger.getCronExpression());

        System.out.println("---------------------------------------------------------------------------------------");

        //job6 每周 星期一到星期五 每隔30分钟，运行一次
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job6","group1").build();

        trigger = TriggerBuilder.newTrigger().withIdentity("trigger6", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0,30 * * ? * MON-FRI")).build();

        ft = scheduler.scheduleJob(job,trigger);
        System.out.println(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
                + trigger.getCronExpression());

        System.out.println("---------------------------------------------------------------------------------------");

        // job 7 每周 星期六到星期日 每隔30分钟，运行一次
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job7", "group1").build();

        trigger = TriggerBuilder.newTrigger().withIdentity("trigger7", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0,30 * * ? * SAT,SUN")).build();

        ft = scheduler.scheduleJob(job, trigger);
        System.out.println(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
                + trigger.getCronExpression());


        scheduler.start();
        System.out.println("--------------------------等5分钟-----------------");
        try {
            // wait five minutes to show jobs
            Thread.sleep(300L * 1000L);
            // executing...
        } catch (Exception e) {
            //
        }

        scheduler.shutdown(true);
        SchedulerMetaData metaData = scheduler.getMetaData();
        System.out.println("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
    }

    public static void main(String[] args) throws Exception {
        CronTriggerExample cronTriggerExample = new CronTriggerExample();
        cronTriggerExample.run();
    }
}
