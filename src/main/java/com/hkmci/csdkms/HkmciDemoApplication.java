package com.hkmci.csdkms;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.hkmci.csdkms.common.SummarizeScore;
import com.hkmci.csdkms.controller.BannerController;
import com.hkmci.csdkms.controller.ReportController;
import com.hkmci.csdkms.service.PoolSizeChecker;
import com.hkmci.csdkms.service.ResourceHitRateService;
import com.hkmci.csdkms.storage.StorageProperties;
import com.hkmci.csdkms.storage.StorageService;


/**
 * @author Holfer ZHANG
 * @Description
 * @project HKMCIDemo
 * @package com.hkmci
 * @email holfer.zhang@hkmci.com
 * @date 2019/03/04
 */

@SpringBootApplication
//
@EnableJpaRepositories(bootstrapMode = BootstrapMode.DEFERRED)
@EnableScheduling
@EnableAsync
@EnableCaching
@EnableTransactionManagement
@EnableConfigurationProperties(StorageProperties.class)
public class HkmciDemoApplication {

	
	@Autowired
	@Resource
    private SummarizeScore summarizeScore;
	
	
	@Autowired
	@Resource
	private ResourceHitRateService resourceHitRateService;
	
	
	public static void main(String[] args) throws IOException {
		new File(BannerController.uploadDir).mkdirs();
		SpringApplication.run(HkmciDemoApplication.class, args);
		
		try {
			System.out.println("Hello");
			System.out.println("java.library.path: " + System.getProperty("java.library.path") );
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
/* This scheduled job will start at every day 0101	*/
	@Scheduled(cron = "0 10 "
			+ " 01 * * ?")
	//This is for testing, will do scheduled job every hour
	//   @Scheduled(cron = "0 0 0/1 1/1 * ?")
	public void scheduleTaskUsingCronExpression() {
	  
	    long now = System.currentTimeMillis() / 1000;
	    System.out.println(
	      "schedule tasks using cron jobs - " + now);
	    summarizeScore.run();
	  //  resourceHitRateService.countAndSaveResourceHitRate();
	}
}


