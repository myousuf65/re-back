package com.hkmci.csdkms.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.ResourceHitRate;
import com.hkmci.csdkms.repository.LogRepository;
import com.hkmci.csdkms.repository.ReosurceHitRateRepository;

@Service
public class ResourceHitRateServiceImp implements ResourceHitRateService {
	
	@Autowired
	private LogRepository logRepository;
	
	
	@Autowired
	private ReosurceHitRateRepository resourceHitRateRepository;
	
	public void countAndSaveResourceHitRate() {
		
		Date today = new Date();
		
		Calendar start = Calendar.getInstance();
		start.setTime(today);
		start.add(Calendar.MONTH, -6);
		Date startDate = start.getTime();
//		System.out.println("Start Date (Count Resource Hit Rate) = "+ startDate +" End Date = "+ today);
		
		
		
		List<Object []>query_data=  logRepository.getResourceHitRate(startDate, today);
//		System.out.println("Return Data = "+ query_data.size());
		
		for(Integer i = 0 ;i <query_data.size() ; i++) {
			ResourceHitRate new_record = new ResourceHitRate();
			Object [] data = (Object []) query_data.get(i);
			
//		System.out.println("Resource Id = "+Long.valueOf(data[1].toString())+ " Hit Rate = "+ Integer.valueOf(data[0].toString()));
			if(resourceHitRateRepository.findByResourceId(Long.valueOf(data[1].toString()))==null) {
//				System.out.println(" Hit Rate  = "+data[0].toString() );
				new_record.setHitRate(Integer.valueOf(data[0].toString()));
//				System.out.println(" Resource Id = "+data[1].toString() );
				new_record.setResourceId(Long.valueOf(data[1].toString()));
			}  else {
				new_record= resourceHitRateRepository.findByResourceId(Long.valueOf(data[1].toString()));
				new_record.setHitRate(Integer.valueOf(data[0].toString()));
			}
			resourceHitRateRepository.saveAndFlush(new_record);
		}
		
		
		
	}
	
	public Integer CountResourceHitRate(Long resourceId) {
		Integer HitRate = 0;
		ResourceHitRate getRate = new ResourceHitRate();
		getRate = resourceHitRateRepository.findByResourceId(resourceId);
		if (getRate== null ) {
			
		} else {
		HitRate = getRate.getHitRate();
		}
		 
		return HitRate;
		
	}

	@Override
	public ResourceHitRate updateResourceHitRate(Integer hitRate, Long resourceId) {
		ResourceHitRate rate = new ResourceHitRate();
		rate.setHitRate(hitRate);
		rate.setResourceId(resourceId);
		ResourceHitRate getRate = new ResourceHitRate();
		
		System.out.println("Resource hit rate service impl : line 72- Hit Rate = "+ hitRate +"  Resource id = " + resourceId);
		getRate = resourceHitRateRepository.findByResourceId(resourceId);
				
		
		if(getRate == null) {
			System.out.println("Resource hit rate service impl : line 74- ");
			resourceHitRateRepository.insertHitRate(hitRate, resourceId);
			
		} else {
			System.out.println("Resource hit rate service impl : line 78- ");
			resourceHitRateRepository.updateHitRate(hitRate, resourceId);
		}
		// TODO Auto-generated method stub
		return null;
	}
	
}
