package com.khh.demo1.example9;

import org.quartz.*;

/**
 * Created by Administrator on 2017/7/22.
 */
public class Job1Listener implements JobListener{
    @Override
    public String getName() {
        return "job1_to_job2";
    }

    //Scheduler 在 JobDetail 将要被执行时调用这个方法。
    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        System.out.println("Job1Listener says: Job Is about to be executed.");
    }

    //Scheduler 在 JobDetail 即将被执行，但又被 TriggerListener 否决了时调用这个方法。
    @Override
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
        System.out.println("Job1Listener says: Job Execution was vetoed.");
    }

    //Scheduler 在 JobDetail 被执行之后调用这个方法。
    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        System.out.println("Job1Listener says: Job was executed.");

        JobDetail job2 = JobBuilder.newJob(SimpleJob2.class).withIdentity("job2").build();

        Trigger trigger2 = TriggerBuilder.newTrigger().withIdentity("trigger2").startNow().build();


        try{
            //调度job2马上运行
            jobExecutionContext.getScheduler().scheduleJob(job2,trigger2);
        }catch (Exception e1){
            System.out.println("Unable to schedule job2");
            e.printStackTrace();
        }

    }
}
