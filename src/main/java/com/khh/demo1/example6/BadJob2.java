package com.khh.demo1.example6;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import java.util.Date;

/**
 * Created by Administrator on 2017/7/22.
 * 一个将抛出执行异常的哑工作
 *
 */
public class BadJob2 implements Job {

    private int calculation;

    public BadJob2(){

    }

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        JobKey key = jobExecutionContext.getJobDetail().getKey();

        System.out.println("---" + key + " executing at " + new Date());

        //一个认为的实例如下：
        //由于被除数为0，而抛出异常(仅在第一次运行)
        try{
            int zero = 0;
            calculation = 4815 / zero;
        }catch (Exception e){
            JobExecutionException e2 = new JobExecutionException(e);

            //quartz 会自动停止触发所有与BadJob2的触发器
            //所以它不会再运行
            e2.setUnscheduleAllTriggers(true);
            throw e2;
        }

        System.out.println("---" + key + " completed at " + new Date());

    }
}
