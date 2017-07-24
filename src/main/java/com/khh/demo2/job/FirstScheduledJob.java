package com.khh.demo2.job;

import com.khh.demo2.bean.AnotherBean;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by FSTMP on 2017/7/24.
 */
public class FirstScheduledJob extends QuartzJobBean {

    private AnotherBean anotherBean;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("I am FirstScheduledJob");
        this.anotherBean.printAnotherMessage();
    }

    public void setAnotherBean(AnotherBean anotherBean) {
        this.anotherBean = anotherBean;
    }
}
