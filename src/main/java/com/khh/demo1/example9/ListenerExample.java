package com.khh.demo1.example9;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;

/**
 * Created by Administrator on 2017/7/22.
 *
 *  我们通过matcher让不同的监听器监听不同的任务。它有很多实现类，先逐一分析如下：
 *
 *  1. KeyMatcher<JobKey> 根据JobKey进行匹配，每个JobDetail都有一个对应的JobKey,里面存储了JobName和JobGroup来定位唯一的JobDetail。它的常用方法有：
 *
 *      ************构造Matcher方法************
 *      KeyMatcher<JobKey> keyMatcher = KeyMatcher.keyEquals(pickNewsJob.getKey());//构造匹配pickNewsJob中的JobKey的keyMatcher。
 *
 *      *********使用方法************
 *      scheduler.getListenerManager().addJobListener(myJobListener, keyMatcher);//通过这句完成我们监听器对pickNewsJob的唯一监听
 *
 *
 *
 *  2.GroupMatcher  根据组名信息匹配，它的常用方法有：
 *
 *   GroupMatcher<JobKey> groupMatcher = GroupMatcher.jobGroupContains("group1");//包含特定字符串
 *   GroupMatcher.groupEndsWith("oup1");//以特定字符串结尾
 *   GroupMatcher.groupEquals("jgroup1");//以特定字符串完全匹配
 *   GroupMatcher.groupStartsWith("jgou");//以特定字符串开头
 *
 *  3.AndMatcher  对两个匹配器取交集，实例如下：
 *
 *      KeyMatcher<JobKey> keyMatcher = KeyMatcher.keyEquals(pickNewsJob.getKey());
 *      GroupMatcher<JobKey> groupMatcher = GroupMatcher.jobGroupContains("group1");
 *      AndMatcher<JobKey> andMatcher = AndMatcher.and(keyMatcher,groupMatcher);//同时满足两个入参匹配
 *
 *  4.OrMatcher  对两个匹配器取并集，实例如下：
 *
 *      OrMatcher<JobKey> orMatcher = OrMatcher.or(keyMatcher, groupMatcher);//满足任意一个即可
 *
 *  5.EverythingMatcher 局部全局匹配，它有两个构造方法：
 *
 *      EverythingMatcher.allJobs();//对全部JobListener匹配
 *      EverythingMatcher.allTriggers();//对全部TriggerListener匹配
 *
 */
public class ListenerExample {

    public void run() throws Exception{

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDetail job = JobBuilder.newJob(SimpleJob1.class).withIdentity("job1").build();

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1").startNow().build();


        //创建一个监听器,并设置它
        Job1Listener listener = new Job1Listener();

        //KeyMatcher<JobKey>根据JobKey进行匹配，每个JobDetail都有一个对应的JobKey,里面存储了JobName和JobGroup来定位唯一的JobDetail。

        KeyMatcher<JobKey> keyMatcher = KeyMatcher.keyEquals(job.getKey());//构造匹配job的JobKey的keyMatcher

        scheduler.getListenerManager().addJobListener(listener,keyMatcher);//通过这句完成我们监听器对job的唯一监听


        scheduler.scheduleJob(job,trigger);


        //作业开始调度
        scheduler.start();
        System.out.println("----------------------scheduler start-------------------");

        //wait 30 seconds
        //note:nothing will run
        try{
            Thread.sleep(30L * 1000L);
        }catch (Exception e){

        }
        scheduler.shutdown(true);
        System.out.println("----------------------scheduler shutdown-------------------");

        SchedulerMetaData metaData = scheduler.getMetaData();
        System.out.println("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");




    }

    public static void main(String[] args) throws Exception{
        ListenerExample example = new ListenerExample();
        example.run();
    }
}
