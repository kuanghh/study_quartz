package com.khh.demo1.example8;


import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.AnnualCalendar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * calendar ： 日历
 * Created by Administrator on 2017/7/22.
 * 此示例将演示如何使用calendars来排除不应该进行调度的时间
 *
 * 下面程序可以使job每个小时调用一次，但在固定的2005.6.4，2005.9.31，2005.12.25将不会触发调度
 */
public class CalendarExample {

    public void run() throws Exception{

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();


        /**
         * 所有的Calendar既可以是排除，也可以是包含，取决于：
         *
         *   •HolidayCalendar。指定特定的日期，比如20140613。精度到天。
         *   •DailyCalendar。指定每天的时间段（rangeStartingTime, rangeEndingTime)，格式是HH:MM[:SS[:mmm]]。也就是最大精度可以到毫秒。
         *   •WeeklyCalendar。指定每星期的星期几，可选值比如为java.util.Calendar.SUNDAY。精度是天。
         *   •MonthlyCalendar。指定每月的几号。可选值为1-31。精度是天
         *   •AnnualCalendar。 指定每年的哪一天。使用方式如上例。精度是天。
         *   •CronCalendar。指定Cron表达式。精度取决于Cron表达式，也就是最大精度可以到秒
         */
        //将 holidays 添加到schedule中
        AnnualCalendar holidays = new AnnualCalendar();

        //7月4日
        Calendar fourthOfJuly = new GregorianCalendar(2005,6,4);
        holidays.setDayExcluded(fourthOfJuly,true);//排除2005.6.4

        //10月31日
        Calendar halloween = new GregorianCalendar(2005,10,31);
        holidays.setDayExcluded(halloween,true);//排除2005.10.31

        //12月25日
        Calendar christmas = new GregorianCalendar(2005,12,25);
        holidays.setDayExcluded(christmas,true);//排除2005.12.25


        //告诉触发器 关于我们holiday的日期
        scheduler.addCalendar("holidays",holidays,false,false);

        //从10.31开始，安排一个工作，每1小时运行
        //at 10 am
        Date runDate = DateBuilder.dateOf(0,0,10,31,10);

        JobDetail job = JobBuilder.newJob(SimpleJob.class).withIdentity("job1", "group1").build();

        SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startAt(runDate)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(1).repeatForever())
                .modifiedByCalendar("holidays").build();

        //安排作业并打印第一个运行日期
        Date firstRunTime = scheduler.scheduleJob(job, trigger);


        System.out.println(job.getKey() + " will run at: " + firstRunTime + " and repeat: " + trigger.getRepeatCount()
                + " times, every " + trigger.getRepeatInterval() / 1000 + " seconds");

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
        new CalendarExample().run();
    }
}
