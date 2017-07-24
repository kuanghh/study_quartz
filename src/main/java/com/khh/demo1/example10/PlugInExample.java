package com.khh.demo1.example10;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.impl.StdSchedulerFactory;


/**
 * Created by FSTMP on 2017/7/24.
 * //这个例子将产生大量的作业来运行
 */
public class PlugInExample {

    public void run() throws Exception {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = null;

        try {
            System.getProperties().setProperty("org.quartz.properties", "demo1/example10/quartz.properties");
            scheduler = sf.getScheduler();//如果没有上面一行的话，这里默认加载resources下的quartz.xml文件
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        scheduler.start();

        try{
            Thread.sleep(30L * 1000L);
        }catch(Exception e){
            e.printStackTrace();
        }
        scheduler.shutdown(true);

        SchedulerMetaData metaData = scheduler.getMetaData();
        System.out.println("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
    }
    public static void main(String[] args) throws Exception{

        new PlugInExample().run();
    }
}
