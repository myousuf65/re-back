package com.hkmci.csdkms.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hkmci.csdkms.entity.Log;
import com.hkmci.csdkms.service.LogService;

@CrossOrigin
@RestController
@RequestMapping("/log")
public class LogController {
	@Autowired
	// private User user;
	@Resource
	private LogService logService;

	@RequestMapping("/all")
	public ResponseEntity<JsonResult> getList(HttpSession session) {
		// model.addAttribute("user", userRepository.findAll());

		return JsonResult.ok(logService.findAll(),session);
	}

	@RequestMapping("/")
	public ResponseEntity<JsonResult> getAll(HttpSession session) {
		// model.addAttribute("user", userRepository.findAll());

		return JsonResult.ok(logService.findAll(),session);
	}

	@RequestMapping("/getById/{id}")
	public ResponseEntity<JsonResult> findByID(@PathVariable Long id,HttpSession session) {

		return JsonResult.ok(logService.findById(id),session);
	}

	@RequestMapping("/add/{id}")
	public ResponseEntity<JsonResult> addLog(@PathVariable Long id,HttpSession session) throws Exception, Throwable {

		// Optional<Log> temp_data = logService.findById((long) 1);
		// HttpServletRequest req,
		Log new_log = new Log();
		new_log.setCreatedBy(id);
		new_log.setCreatedAt(new Date());
		new_log.setPkid(id);
		new_log.setLogtype(id);
		new_log.setRemark("This is test add " + id);
		new_log.setResult("SUCCESS " + id);
		new_log.setTableId(id);
		new_log.setUinstId(id);
		new_log.setUrankId(id);
		new_log.setUsectionId(id);

		Log return_log = logService.newLog(new_log);
		return JsonResult.ok(return_log,session);
	}

}
