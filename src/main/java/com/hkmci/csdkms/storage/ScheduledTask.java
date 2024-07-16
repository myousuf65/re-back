package com.hkmci.csdkms.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Description: Cronjobs
 * Designer: holfer
 * Date: 2019/6/18
 * Version: 1.0.0
 */
@Component
public class ScheduledTask {

    private Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    private int fixedDelayCount = 1;
    private int fixedRateCount = 1;
    private int initialDelayCount = 1;
    private int cronCount = 1;

//    @Scheduled(fixedDelay = 5000)        //fixedDelay = 5000 means after 5000ms，Spring scheduling will call this function again
//    public void testFixDelay() {
//        logger.info("===fixedDelay: The {} Times to run", fixedDelayCount++);
//    }
//
//    @Scheduled(fixedRate = 5000)        //fixedRate = 5000 means after 5000ms，Spring scheduling will call this function again
//    public void testFixedRate() {
//        logger.info("===fixedRate: The {} Times to run", fixedRateCount++);
//    }
//
//    @Scheduled(initialDelay = 1000, fixedRate = 5000)   //initialDelay = 1000 means after 1000ms, first time to run this function
//    public void testInitialDelay() {
//        logger.info("===initialDelay: The {} Times to run", initialDelayCount++);
//    }
//
//    @Scheduled(cron = "0 0/1 * * * ?")  //cron express
//    public void testCron() {
//        logger.info("===initialDelay: The {} Times to run", cronCount++);
//    }

}
