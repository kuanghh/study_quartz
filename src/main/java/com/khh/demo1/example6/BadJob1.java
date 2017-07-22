package com.khh.demo1.example6;

import org.quartz.*;

import java.util.Date;

/**
 * Created by Administrator on 2017/7/22.
 *
 * 一个将抛出执行异常的哑工作
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class BadJob1 implements Job{

    private int calculation;

    public BadJob1(){}


    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey key = context.getJobDetail().getKey();

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        int denominator = jobDataMap.getInt("denominator");

        System.out.println("---" + key + " executing at " + new Date() + " with denominator " + denominator);

        //一个认为的实例如下：
        //由于被除数为0，而抛出异常(仅在第一次运行)
        try{
            calculation = 4815 / denominator;
        }catch (Exception e){
            System.out.println("error job :" + key);
            JobExecutionException e2 = new JobExecutionException(e);

            //固定分母，以便下次正常运行
            jobDataMap.put("denominator","1");

            //这项工作将被立即触发
            e2.setRefireImmediately(true);
            throw e2;
        }

        System.out.println("---" + key + " completed at " + new Date());
    }
}
