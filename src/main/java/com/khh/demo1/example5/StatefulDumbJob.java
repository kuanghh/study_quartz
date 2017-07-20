package com.khh.demo1.example5;

import org.quartz.*;

/**
 * Created by FSTMP on 2017/7/20.
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class StatefulDumbJob implements Job {

    public static final String NUM_EXECUTIONS = "NumExecutions";

    public static final String EXECUTIONS_DELAY = "ExecutionDelay";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}
