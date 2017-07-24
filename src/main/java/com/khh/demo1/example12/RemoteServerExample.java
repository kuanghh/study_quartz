package com.khh.demo1.example12;

import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by FSTMP on 2017/7/24.
 *
 * 服务端，去调度客户端
 */
public class RemoteServerExample {

    public void run() throws Exception{
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = null;
        try{
            System.getProperties().setProperty("org.quartz.properties","demo1/example12/server.properties");
            scheduler = schedulerFactory.getScheduler();
        }catch (Exception e){
            e.printStackTrace();
        }

        scheduler.start();
        System.out.println("--------------------server scheduler start-------------------");
        try{
            Thread.sleep(600L * 1000L);
        }catch(Exception e){
            e.printStackTrace();
        }
        scheduler.shutdown(true);
        System.out.println("--------------------server scheduler shutdown-------------------");

        SchedulerMetaData metaData = scheduler.getMetaData();
        System.out.println("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
        
    }

    public static void main(String[] args) throws Exception{
        new RemoteServerExample().run();
    }
}
