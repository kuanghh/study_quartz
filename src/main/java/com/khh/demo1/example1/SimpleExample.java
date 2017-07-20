package com.khh.demo1.example1;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * 本例将演示如何启动和关闭quartz调度器，以及如何调度运行中的作业。
 * Created by FSTMP on 2017/7/20.
 */
public class SimpleExample {

    public void run() throws Exception{

        //获取Scheduler对象
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        Date date = new Date();
        System.out.println("现在的时间是: " + date);
        //计算机在下一个回合分钟的时间
        Date start = DateBuilder.evenMinuteDate(date);

        //定义工作并把它与HelloJob.class联系起来
        JobDetail job = JobBuilder.newJob(HelloJob.class).withIdentity("job1", "group1").build();

        //触发作业在下一个回合运行
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("job1", "group1").startAt(start).build();

        //告诉quartz使用我们的触发器来安排作业
        scheduler.scheduleJob(job,trigger);
        System.out.println(job.getKey() + " will run at : " + start);
        //启动
        scheduler.start();

        try {
            // 等待65秒
            Thread.sleep(65L * 1000L);
            // executing...
        } catch (Exception e) {
            //
        }
        //关闭
        scheduler.shutdown(true);
    }

    public static void main(String[] args) throws Exception{
        new SimpleExample().run();
    }
}
