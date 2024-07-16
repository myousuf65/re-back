package com.hkmci.csdkms.storage;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.hkmci.csdkms.entity.Log;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.service.LogService;

public class Logger {
	
	@Autowired
	@Resource
	private LogService logService;
	
	public Log login(User user, Long pkid, String remark, String result) {
		
		Long table_id = 1L;
		Long logType = 1L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);

		Log return_log = logService.newLog(loginLog);
		return return_log;
		
	}
	
	public Log viewResource(User user, Long pkid, String remark, String result) {
		
		Long table_id = 1L;
		Long logType = 1L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);

		Log return_log = logService.newLog(loginLog);
		return return_log;
		
	}
	
	public Log downloadResource(User user, Long pkid, String remark, String result) {
		
		Long table_id = 1L;
		Long logType = 1L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);

		Log return_log = logService.newLog(loginLog);
		return return_log;
		
	}

}
