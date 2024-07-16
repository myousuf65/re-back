package com.hkmci.csdkms.controller;
//package com.hkmci.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.hkmci.controller.JsonResult;
//
//
//
//@Controller
//@RequestMapping("err")
//public class ErrorController {
//
//	@RequestMapping("/error")
//	public String error() {
//		
//		int a = 1 / 0;
//		System.out.println(a);
//		return "thymeleaf/error";
//	}
//	
//	@RequestMapping("/ajaxerror")
//	public String ajaxerror() {
//		
//		return "thymeleaf/ajaxerror";
//	}
//	
//	@RequestMapping("/getAjaxerror")
//	@ResponseBody
//	public JsonResult getAjaxerror() {
//		
//		int a = 1 / 0;
//		System.out.println(a);
//		return JsonResult.ok();
//	}
//}