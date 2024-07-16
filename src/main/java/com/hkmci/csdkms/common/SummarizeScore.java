package com.hkmci.csdkms.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.controller.ReportController;
import com.hkmci.csdkms.entity.Log;
import com.hkmci.csdkms.entity.ScoreLog;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.report.ReportService;
import com.hkmci.csdkms.repository.ScoreLogRepository;
import com.hkmci.csdkms.repository.UserRepository;
import com.hkmci.csdkms.service.LogService;
import com.hkmci.csdkms.storage.StorageService;



@Service
public class SummarizeScore  extends TimerTask {
		@Autowired
		@Resource
		private LogService logger;
		
		@Autowired
		@Resource
	    private Common common;
		
		@Autowired
		@Resource
	    private ReportService reportService;
		
		@Autowired
		@Resource 
		private LogService logService;
	   

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ScoreLogRepository scoreLogRepository;
	
	private static ReportController reportController;
	
	
	  public void run() {
//		    System.out.println("Generating report");
//		    //TODO generate report
//		   // reportController.summarizeScore();
//		    
//		    
//		    
//		    
//			
			System.out.println("summarize score run()");
				 
			Date queryDate = new Date();
			//System.out.println("Query Date = "+queryDate );
			Calendar calendar = Calendar.getInstance(); //Calendar
			calendar.setTime(new Date());//Calendar's Current Date
			calendar.add(Calendar.DAY_OF_YEAR,-1);  
			queryDate = calendar.getTime();   
			SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date yesterday;
			try {
				yesterday = ymdFormat.parse(ymdFormat.format(new Date(queryDate.getTime())));
			} catch (Exception e) {
				yesterday = null;
			}
			
			
			//System.out.println("Summarize Score Yesterday = "+ yesterday);
			Date queryDate1 = new Date();
			Calendar calendar1 = Calendar.getInstance(); //Calendar
			calendar1.setTime(new Date());//Calendar's Current Date
			calendar1.add(Calendar.DAY_OF_YEAR,0);  
			queryDate1 = calendar1.getTime();   
			SimpleDateFormat ymdFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			Date today;
			try {
				today = ymdFormat.parse(ymdFormat1.format(new Date(queryDate1.getTime())));
			} catch (Exception e) {
				today = null;
			}

			//System.out.println("Yesterday = "+ yesterday + " Today = "+ today);
			
			

//			
			List<User> user = userRepository.findAllByIsDeleted(0);
			
			
			//System.out.println("How many User = "+user.size());
			Date startDate = yesterday;
			Date endDate = today;
//			
//			user.forEach(System.out::println);
			for(Integer i = 0; i<user.size();i++) {
		
				Integer score =0;
		
				
				List<Object[]> detail  = scoreLogRepository.getScoreDetail1(user.get(i).getId(), startDate, endDate);
					
				System.out.println("Summarize Score Line 118 : detail size  ="+ detail.size());
				for(Integer j =0 ; j <detail.size();j++) {
				
						Object[] data = (Object[]) detail.get(j);
						score += Integer.valueOf(data[2].toString());
					}
				
				System.out.println("Score = "+ score + "; User Id = " + user.get(i).getId());
				
			 if(score>30) {
					
				 //System.out.println("User Id = "+ user.get(i).getId()+" Score = "+ score+" Yesterday = "+ yesterday +" Today = "+ today);
				 Integer minus = 30-score;
				 //System.out.println("Minus "+ minus+"User "+user.get(i).getId()+" Inst id = "+user.get(i).getInstitutionId() + " Rank Id "+ user.get(i).getRankId()+" Section Id "+ user.get(i).getSectionId()+"Staff No ="+ user.get(i).getStaffNo());
					
					Date setYesterdayTime = yesterday;
					//System.out.println("Query Date = "+setYesterdayTime );
					Calendar calendar3 = Calendar.getInstance(); //Calendar
					calendar.setTime(setYesterdayTime);//Calendar's Current Date
					calendar.add(Calendar.HOUR_OF_DAY,1);  
					queryDate = calendar.getTime();   
					SimpleDateFormat ymdFormat3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date yesterday3;
					try {
						yesterday3 = ymdFormat3.parse(ymdFormat3.format(new Date(queryDate.getTime())));
					} catch (Exception e) {
						yesterday3 = null;
					}
			
				 
				 ScoreLog scoreLog = new ScoreLog(1L,user.get(i),minus,yesterday3);
//				 System.out.println("Summzrize Score Line 146:  Score Log ="+ scoreLog.getLogId()+" score = "+minus);
//				 logger.saveScoreLog(scoreLog);
				 try {
					Thread.sleep(2000);
					 logger.saveScoreLog(scoreLog);
					 System.out.println("Summzrize Score Line 151:User id =  "+user.get(i).getId()+ " Score Log ="+ scoreLog.getLogId()+" score = "+minus+" current time = "+ new Date());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 
			 Integer UserScore = logService.getUserScore(user.get(i).getId());
			 //System.out.println("User Score = "+ UserScore);
			 user.get(i).setScore(UserScore);
			 userRepository.saveAndFlush(user.get(i));
				
			}
			
			
		  }
	
//	@Override
//	public void run() {
		// TODO Auto-generated method stub
		
		
//		 
//	    Timer timer = new Timer();
//	    
//	      Calendar calendar = Calendar.getInstance();
//	      calendar.set(Calendar.HOUR_OF_DAY, 11);
//	      calendar.set(Calendar.MINUTE, 59);
//	      calendar.set(Calendar.SECOND, 00);
//	      Date time = calendar.getTime();
//	      
//	      timer.schedule(reportController.summarizeScore(),
//	                     time);    
//	   
//	}
//	
//	@Bean
//    CommandLineRunner init(StorageService storageService) {
//        return (args) -> {
//            //storageService.deleteAll();
//            storageService.init();
//        };
//    }
//	
//		

	
	
	
}


