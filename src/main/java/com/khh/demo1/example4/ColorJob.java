package com.khh.demo1.example4;

import org.quartz.*;

import java.util.Date;

/**
 * Created by FSTMP on 2017/7/20.
 * 这只是一个接收参数和维护状态的简单工作。
 * 如果你想在 某个job执行的时候传入参数,参数在job执行过程中对参数有所修改,并且在job执行完毕后把参数返回
 *  那么你需要学习一下现在的这个例子了,因为它正是你所想要的
 */
@PersistJobDataAfterExecution//保存在JobDataMap传递的参数
@DisallowConcurrentExecution//保证多个任务间不会同时执行.所以在多任务执行时最好加上
public class ColorJob implements Job{

    public static final String FAVORITE_COLOR = "favorite_color";
    public static final String EXECUTION_COUNT = "count";

    /**
     * 由于Quartz每次执行时都将重新实例化一个类，成员不能使用非静态成员变量来维护状态！
      */
    private  int _counter = 1;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        //这项工作只是打印出它的工作名称和正在运行的日期和时间
        JobKey jobKey = jobExecutionContext.getJobDetail().getKey();

        //抓取和打印传递参数
        JobDataMap data = jobExecutionContext.getJobDetail().getJobDataMap();
        String favoriteColor = data.getString(FAVORITE_COLOR);

        int count = data.getInt(EXECUTION_COUNT);

        System.out.println("ColorJob: " + jobKey + " executing at " + new Date() + "\n" +
                "  favorite color is " + favoriteColor + "\n" +
                "  execution count (from job map) is " + count + "\n" +
                "  execution count (from job member variable) is " + _counter);

        //增加计数并将其存储回
        //作业映射，以便正确维护作业状态。
        count++;
        data.put(EXECUTION_COUNT,count);

        //增加本地成员变量
        //由于工作状态不能通过成员变量来维护，所以这没有真正的用途。
        _counter++;
    }
}
