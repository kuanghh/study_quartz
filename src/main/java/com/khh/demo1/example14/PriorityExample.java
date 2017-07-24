package com.khh.demo1.example14;

import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by FSTMP on 2017/7/24.
 */
public class PriorityExample {

    public void run() throws Exception{

        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

    }

    public static void main(String[] args) {

    }
}
