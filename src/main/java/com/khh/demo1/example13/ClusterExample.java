package com.khh.demo1.example13;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 *
 * 这个例子演示了如何在集群环境中使用quartz来支持故障转移。
 * 在集群环境中，您使用共享调度器运行多个石英实例。
 * 这个共享调度器保存在数据库中，因此两个实例可以共享相同的调度数据。
 *
 *
 * 注意：当您运行客户机和服务器时，此示例运行最好运行在不同的电脑。
 * 但是，您当然可以运行服务器和客户机在同一台电脑上的！
 *
 * Created by FSTMP on 2017/7/24.
 * 用于测试/显示jdbcjobstore聚类的特征（JobStoreTX、JobStoreCMT）。
 *
 * 所有实例必须使用不同的属性文件，因为它们的实例ID必须是不同的，但是所有其他属性都应该相同。
 *
 * 如果你想离开现有的工作和触发器，通过命令行参数称为“clearjobs”。
 *
 * 您可能应该从一组“新”的表开始（假设您可能从其他测试中获得了一些数据），因为从非集群设置与集群化的数据混合可能是坏的。
 *
 * 尝试在运行过程中杀死其中一个集群实例，并查看剩下的实例恢复正在进行的作业。注意，检测失败可能需要15秒左右的默认设置。
 *
 * 也可以尝试使用/不使用调度程序注册的关闭钩子插件运行它。
 *
 */
public class ClusterExample {

    public void run(boolean inClearJobs,boolean inScheduleJobs) throws Exception{

        System.getProperties().setProperty("org.quartz.properties","demo1/example13/instance1.properties");
//        System.getProperties().setProperty("org.quartz.properties","instance2.properties");
        // First we must get a reference to a scheduler
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        if (inClearJobs) {
            System.out.println("********* Deleting existing jobs/triggers ***********");
            sched.clear();
        }

        System.out.println("------- Initialization Complete -----------");

        if(inScheduleJobs) {
            System.out.println("------- Scheduling Jobs ------------------");

            String schedId = sched.getSchedulerInstanceId();

            int count = 1;
            //将触发器放在集群节点实例中命名的组中，以便区分（在日志中）调度程序从何处调度，如果调度程序正在进行时重新执行该作业…
            JobDetail job = JobBuilder.newJob(SimpleRecoveryJob.class)
                    .withIdentity("job_" + count, schedId).requestRecovery().build();

            SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger_" + count, schedId)
                    .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(20).withIntervalInSeconds(5)).build();

            System.out.println(job.getKey() + " will run at: " + trigger.getNextFireTime() + " and repeat: "
                    + trigger.getRepeatCount() + " times, every " + trigger.getRepeatInterval() / 1000 + " seconds");

            sched.scheduleJob(job, trigger);
            count++;

            //-----------------------------------------------------------------------------------------------------------------------------

            job = JobBuilder.newJob(SimpleRecoveryJob.class).withIdentity("job_" + count, schedId).requestRecovery().build();

            trigger = TriggerBuilder.newTrigger().withIdentity("trigger_" + count, schedId)
                    .startAt(DateBuilder.futureDate(2, DateBuilder.IntervalUnit.SECOND))
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(20).withIntervalInSeconds(5)).build();

            System.out.println(job.getKey() + " will run at: " + trigger.getNextFireTime() + " and repeat: "
                    + trigger.getRepeatCount() + " times, every " + trigger.getRepeatInterval() / 1000 + " seconds");

            sched.scheduleJob(job, trigger);
            count++;
            //-----------------------------------------------------------------------------------------------------------------------------

            job = JobBuilder.newJob(SimpleRecoveryStateFulJob.class).withIdentity("job_" + count, schedId).requestRecovery().build();

            trigger = TriggerBuilder.newTrigger().withIdentity("trigger_" + count, schedId)
                    .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(20).withIntervalInSeconds(3)).build();

            System.out.println(job.getKey() + " will run at: " + trigger.getNextFireTime() + " and repeat: "
                    + trigger.getRepeatCount() + " times, every " + trigger.getRepeatInterval() / 1000 + " seconds");

            sched.scheduleJob(job, trigger);
            count++;
            //-----------------------------------------------------------------------------------------------------------------------------

            job = JobBuilder.newJob(SimpleRecoveryJob.class).withIdentity("job_" + count, schedId).requestRecovery().build();

            trigger = TriggerBuilder.newTrigger().withIdentity("trigger_" + count, schedId)
                    .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(20).withIntervalInSeconds(4)).build();

            System.out.println(job.getKey() + " will run at: " + trigger.getNextFireTime() + " and repeat: "
                    + trigger.getRepeatCount() + " times, every " + trigger.getRepeatInterval() / 1000 + " seconds");

            sched.scheduleJob(job, trigger);
            count++;
            //-----------------------------------------------------------------------------------------------------------------------------

            job = JobBuilder.newJob(SimpleRecoveryJob.class).withIdentity("job_" + count, schedId).requestRecovery().build();

            trigger = TriggerBuilder.newTrigger().withIdentity("trigger_" + count, schedId)
                    .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(20).withIntervalInMilliseconds(4500L)).build();

            System.out.println(job.getKey() + " will run at: " + trigger.getNextFireTime() + " and repeat: "
                    + trigger.getRepeatCount() + " times, every " + trigger.getRepeatInterval() / 1000 + " seconds");

            sched.scheduleJob(job, trigger);

        }

        sched.start();
        System.out.println("----------------------scheduler start-------------------");
        try{
            Thread.sleep(3600L * 1000L);
        }catch(Exception e){
            e.printStackTrace();
        }
        sched.shutdown(true);
        System.out.println("----------------------scheduler shutdown-------------------");
        SchedulerMetaData metaData = sched.getMetaData();
        System.out.println("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");

    }

    public static void main(String[] args) throws Exception{
        boolean clearJobs = false;
        boolean scheduleJobs = true;

        ClusterExample example = new ClusterExample();
        example.run(clearJobs, scheduleJobs);
    }
}
