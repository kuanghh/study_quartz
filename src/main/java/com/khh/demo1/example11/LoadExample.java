package com.khh.demo1.example11;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * Created by FSTMP on 2017/7/24.
 *
 * 这个例子演示了quartz是如何处理大的。
 * 工作数。这个例子从500个作业开始。然而,
 * 这个数字可以通过修改启动脚本来更改。
 * 由于线程池的大小（此示例用作线程）
 * 计数为12），只有12个线程同时运行在
 * 调度程序。
 */
public class LoadExample {

    private int _numberOfJobs = 500;

    public LoadExample(int _numberOfJobs){
        this._numberOfJobs = _numberOfJobs;
    }

    public void run() throws Exception{

        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = null;
        try{
            System.getProperties().setProperty("org.quartz.properties","demo1/example11/quartz.properties");
            scheduler = schedulerFactory.getScheduler();
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("------- Initialization Complete -----------");

        //调度500个作业运行
        for (int i = 0; i < _numberOfJobs; i++) {

            //requestRecovery() ----> 如果遇到“恢复”或“失败”的情况，指示调度程序是否应该重新执行该作业，默认为false
            //当作业在运行时，调度失败，请重新执行该作业
            //本人觉得requestRecovery应该有true属性，但官方文档是选择默认false属性的
            JobDetail job = JobBuilder.newJob(SimpleJob.class).withIdentity("job" + i, "group_1").requestRecovery().build();

            long timeDalay = (long) (Math.random() * 25000);
            job.getJobDataMap().put(SimpleJob.DELAY_TIME,timeDalay);

            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger" + i, "group_1").
                    startAt(DateBuilder.futureDate((10000 + (i * 100)), DateBuilder.IntervalUnit.MILLISECOND)).build();

            Date tf = scheduler.scheduleJob(job, trigger);
            if (i % 25 == 0) {
                System.out.println("...scheduled " + i + " jobs");
            }
        }

        scheduler.start();
        System.out.println("----------------------scheduler start-------------------");
        try{
            Thread.sleep(300L * 1000L);
        }catch(Exception e){
            e.printStackTrace();
        }
        scheduler.shutdown(true);
        System.out.println("----------------------scheduler shutdown-------------------");
        SchedulerMetaData metaData = scheduler.getMetaData();
        System.out.println("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");

    }

    public static void main(String[] args)  throws Exception{
        int numberOfJobs = 500;

        new LoadExample(numberOfJobs).run();
    }
}
