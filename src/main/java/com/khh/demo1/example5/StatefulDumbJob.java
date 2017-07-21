package com.khh.demo1.example5;

import org.quartz.*;

import java.util.Date;

/**
 * Created by FSTMP on 2017/7/20.
 *
 * 这个工作为了单元测试的目的
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class StatefulDumbJob implements Job {

    // 静态常量，作为任务在调用间，保持数据的键(key)
    // NUM_EXECUTIONS，保存的计数每次递增1
    // EXECUTION_DELAY，任务在执行时，中间睡眠的时间。本例中睡眠时间过长导致了错失触发
    public static final String NUM_EXECUTIONS = "NumExecutions";
    public static final String EXECUTIONS_DELAY = "ExecutionDelay";

    public StatefulDumbJob(){

    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        System.out.println("---" + context.getJobDetail().getKey()  + " executing.[" + new Date() + "]");

        JobDataMap map = context.getJobDetail().getJobDataMap();

        int executeCount = 0;
        if(map.containsKey(NUM_EXECUTIONS)){
            executeCount = map.getInt(NUM_EXECUTIONS);
        }

        executeCount++;

        map.put(NUM_EXECUTIONS,executeCount);
        //睡眠时间: 由调度类重新设置值 ,本例为 睡眠10s
        long deley = 50001;
        if(map.containsKey(EXECUTIONS_DELAY)){
            deley = map.getLong(EXECUTIONS_DELAY);
        }
        try{
            Thread.sleep(deley);
        }catch (Exception e){

        }
        System.out.println("  -" + context.getJobDetail().getKey() + " complete (" + executeCount + ").");
    }


}
