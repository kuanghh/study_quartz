package com.khh.demo1.example7;

import org.quartz.*;

import java.util.Date;

/**
 * 一个列子 展示了 InterruptableJob
 * Created by Administrator on 2017/7/22.
 */
public class DumbInterruptableJob implements InterruptableJob{


    //当前工作是否被中断?
    private boolean _interrupted = false;

    //工作的名字
    private JobKey _jobKey = null;

    public DumbInterruptableJob(){

    }

    /**
     *由用户中断时调用
     * @throws UnableToInterruptJobException
     */
    @Override
    public void interrupt() throws UnableToInterruptJobException {
        System.out.println("----" + _jobKey + "  -- INTERRUPTING --");
        _interrupted = true;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        _jobKey = jobExecutionContext.getJobDetail().getKey();
        System.out.println("---- " + _jobKey + " executing at " + new Date());

        try{

            //主作业循环，这里通过睡眠来模拟工作
            //因为主程序设置了每5秒触发当前工作，所以这里需要循环4次，每次1秒，来检测当前是否被打断
            for (int i = 0; i < 4; i++) {
                try{
                    Thread.sleep(1000L);
                }catch (Exception ignore){
                    ignore.printStackTrace();
                }

                //定期检查自己是否被打断了
                if(_interrupted){
                    System.out.println("--- " + _jobKey + "  -- Interrupted... bailing out!");
                    return; //这里也能选择抛出JobExecutionException异常
                }
            }

        }finally {
            System.out.println("---- " + _jobKey + " completed at " + new Date());
        }

    }
}
