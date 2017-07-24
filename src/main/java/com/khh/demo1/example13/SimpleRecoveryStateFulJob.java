package com.khh.demo1.example13;

import org.quartz.*;

/**
 * Created by FSTMP on 2017/7/24.
 *
 * 这工作和SimpleRecoveryJob工作的功能一样，只不过这个工作可以持久化数据，并且禁止并发操作
 */
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class SimpleRecoveryStateFulJob extends SimpleRecoveryJob{

    public SimpleRecoveryStateFulJob(){
        super();
    }
}
