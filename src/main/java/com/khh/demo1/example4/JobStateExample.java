package com.khh.demo1.example4;

import org.quartz.DateBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * Created by FSTMP on 2017/7/20.
 */
public class JobStateExample {

    public void run() throws Exception {
        //获取Scheduler对象
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        // 开始时间是下一个10秒
        Date startTime = DateBuilder.nextGivenSecondDate(null, 10);


    }
}
