package com.khh.demo2.anno;

import com.khh.demo2.bean.AnotherBean;
import com.khh.demo2.bean.MyBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by FSTMP on 2017/7/24.
 */
@Component("annoQuartz")
public class AnnoQuartz {

    @Autowired
    private MyBean myBean;

    @Autowired
    private AnotherBean anotherBean;

    @Scheduled(cron = "0/5 * * ? * *")
    public void useMybean(){
        myBean.printMessage();
    }

    @Scheduled(cron = "0/2 * * ? * *")
    public void useAnotherBean(){
        anotherBean.printAnotherMessage();
    }
}
