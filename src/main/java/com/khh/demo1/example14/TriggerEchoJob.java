package com.khh.demo1.example14;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

/**
 * Created by FSTMP on 2017/7/24.
 */
public class TriggerEchoJob implements Job {

    public TriggerEchoJob(){

    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Trigger trigger = context.getTrigger();

        System.out.println("TRIGGER: " + context.getTrigger().getKey());
    }
}
