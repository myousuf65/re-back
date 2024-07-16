package com.hkmci.csdkms.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.SearchEngineModel;
import com.hkmci.csdkms.model.SearchEngineRelatedDocModel;
import com.hkmci.csdkms.service.UserService;

@CrossOrigin
//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/search")
public class SearchController {

    private Logger logger = LoggerFactory.getLogger(SearchController.class);
	
	@Autowired
	@Resource
	private UserService userService;
	
	@Autowired
	@Resource
	private Common common;

    @Value("${app.searchGetTokenURL}")
    private String searchGetTokenURL;
    
    @Value("${app.searchQueryURL}")
    private String searchQueryURL;

    @Value("${app.searchLinkedDocURL}")
    private String searchLinkedDocURL;
    
    //Get Token only by user Id
//	@RequestMapping("/get_token/{user_id}")
//	public void getSecurityToken(@PathVariable Long user_id) {
//		try {
//			Optional<User> user = userService.findById(user_id);
//			String username = user.get().getUsername();
//		     
//		    RestTemplate restTemplate = new RestTemplate();
//		    		    
//		    String result = restTemplate.getForObject(searchGetTokenURL+"?username="+username, String.class);
//		    System.out.println("Search URL:"+ searchGetTokenURL);
//		    System.out.println("Call search token result:"+ result);
//		    
//		    JSONParser parse = new JSONParser(result); 
//		    JSONObject jobj = (JSONObject)parse.parse(); 
//		    String theToken = jobj.getJSONObject("payload").getString("securityInfo");
//
//		    System.out.println("Call search token value:"+ theToken);
//		}catch (Exception e) {
//			e.getStackTrace();
//		}
//	}

    
    
    @RequestMapping("/to-be-redirected")
    public ResponseEntity<Object> redirectToExternalUrl() throws URISyntaxException {
        URI yahoo = new URI("http://www.yahoo.com");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(yahoo);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }
    
    
    @RequestMapping("/query")
//	public RedirectView searchQuery(HttpServletRequest request,
//				@RequestParam(value="keyword",required=true) String keyword) throws Exception{
	public ResponseEntity<JsonResult> searchQuery(@RequestParam(value="user_id",required=true) Long user_id,
		@RequestParam(value="keyword",required=true) String keyword,HttpSession session) throws Exception{
	    //System.out.println("Start query.....");
		try {
			//Get User Name
			Optional<User> user = userService.findById(user_id);
			String username = user.get().getUsername();
//			User user = userService.findByStaffNo((request.getHeader("employeeNumber")));
		   
		    //Get SecurityInfo
		    //System.out.println("Start get token....");
//		    String theToken = getToken(username);
		    String theToken = getToken(user_id);

			String theKeyword = URLEncoder.encode(keyword, "UTF-8");
		    
		    String theRedirectURL = searchQueryURL + "?query=" + theKeyword + "&securityInfo=" + theToken;
		    //System.out.println("theRedirectURL:"+ theRedirectURL);

		    RedirectView redirectView = new RedirectView();
		    redirectView.setUrl(theRedirectURL);
		    return JsonResult.ok(theRedirectURL,session);
		}catch (Exception e) {
			logger.error(new Exception(e).getMessage());
			return null;
		}
	}
    
    
    
 
	@RequestMapping("/related/{user_id}")
	public ResponseEntity<JsonResult>  relatedSearch(@PathVariable Long user_id, @RequestParam(value="resource_url",required=true) String resource_url, @RequestParam(value="return_type",required=true) String return_type,HttpSession session) throws Exception{
//		User user = userService.findByStaffNo((request.getHeader("employeeNumber")));
//		Optional<User> user = userService.findById(user_id);
//		String username = user.get().getUsername();
		   
//	    Get SecurityInfo
//	    String theToken = getToken(username);
	    String theToken = "";
	    try {
	    	theToken = getToken(user_id);
	    }
	    catch (Exception e) {
	    	//System.out.println("related search = Token Error 503");
	    }

		//System.out.println("resource_url - "+resource_url);
		String theURL = URLEncoder.encode(resource_url, "UTF-8");
		
		//** WARNING *** ONLY Replace "KMSUAT" to "KMS" before enable Search Engine Production Server
		//theURL = theURL.replace("kmsuat", "kms");
		//System.out.println("encoded2 - "+theURL);

//	    http://<search-host>/idolweb2/linkedDoc?reference=<encodedDocRef>&output=json&securityInfo=<securityInfo
	    String theRedirectURL = searchLinkedDocURL + "?reference=" + theURL +"&output=json&securityInfo=" + theToken;
	    //System.out.println("theRedirectURL:"+ theRedirectURL);
	    
	    
	    RestTemplate restTemplate = new RestTemplate();
	    String result = "";
	    JsonNode result_in_obj = null;
	    try {
	    	result = restTemplate.getForObject(theRedirectURL, String.class);
	    	result_in_obj = ParsingJsonStringIntoJsonNode(result);
	    }
	    catch (HttpServerErrorException e) {
	    	//System.out.println("HttpServerErrorException Code 503");
	    }
	    
	    List<SearchEngineRelatedDocModel> return_data_resource = new ArrayList<>();
	    List<SearchEngineRelatedDocModel> return_data_blog = new ArrayList<>();
	    
	    if(result_in_obj == null) {
	    	//System.out.println("Related Search Result:" + result);
		    //System.out.println("Related Search Result Obj Status: 500");
	    	return JsonResult.ok(return_data_resource,session);
	    }else {
	    	//System.out.println("Related Search Result:" + result);
		    //System.out.println("Related Search Result Obj Status:" + result_in_obj.get("status"));
		    JsonNode related_list = result_in_obj.get("payload").get("docList");
		    
		    if(related_list == null || related_list.size() == 0) {
		    	return JsonResult.ok(return_data_resource,session);
		    }else {
		    	for(Integer i = 0; i < related_list.size(); i++) {
		    		SearchEngineRelatedDocModel doc_info = new SearchEngineRelatedDocModel();
		    		if(related_list.get(i).get("database").asText().equals("BLOG")) {
		    			Date publishAt = common.blogDateTextToDate(related_list.get(i).get("rmlOtherForm").get("datestring").asText());
		    			doc_info.setId(related_list.get(i).get("rmlOtherForm").get("content/DOCUMENT/BLOG_ID").asLong());
		    			doc_info.setTitle(related_list.get(i).get("rmlOtherForm").get("content/DOCUMENT/BLOG_POST_TITLE").asText());
		    			doc_info.setPublishAt(publishAt);
		    			return_data_blog.add(doc_info);
		    			
		    		}else {
		    			doc_info.setId(related_list.get(i).get("rmlOtherForm").get("content/DOCUMENT/RES_ID").asLong());
				    	doc_info.setTitleEn(related_list.get(i).get("rmlOtherForm").get("content/DOCUMENT/RES_TITLE_EN").asText());
				    	doc_info.setTitleTc(related_list.get(i).get("rmlOtherForm").get("content/DOCUMENT/RES_TITLE_TC").asText());
				    	if(related_list.get(i).get("rmlOtherForm").get("content/DOCUMENT/PARAM_FILETYPE").asText().equals("VIDEO")) {
				    		doc_info.setType("VIDEO");
				    	}else {
				    		doc_info.setType("PDF");
				    	}
				    	
				    	return_data_resource.add(doc_info);
		    		}
			    	
			    }
			    if(return_type.equals("resource")) {
			    	return JsonResult.ok(return_data_resource,session);
			    }else {
			    	return JsonResult.ok(return_data_blog,session);
			    }
		    	
		    }
	    }

	    //create ObjectMapper instance
        //ObjectMapper objectMapper = new ObjectMapper();
	    
//        SearchEngineRelatedDocModel searchEngineRelatedDocInfo;
//		try {
//			searchEngineRelatedDocInfo = objectMapper.readValue(result, SearchEngineRelatedDocModel.class);
//	        System.out.println("Related Search Result:" + searchEngineRelatedDocInfo);
//		}catch (JsonParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	private String getToken(Long user_id) {
	    RestTemplate restTemplate = new RestTemplate();

	    //System.out.println("Search URL:"+ searchGetTokenURL+"?username="+String.valueOf(user_id));

	    String result = restTemplate.getForObject(searchGetTokenURL+"?username="+String.valueOf(user_id), String.class);
	    //System.out.println("Search URL2:"+ searchGetTokenURL);
	    //System.out.println("Call search token result:"+ result);
	    

	    //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //read json file and convert to customer object
        SearchEngineModel searchEngineInfo;
        String theToken=null;
		try {
			searchEngineInfo = objectMapper.readValue(result, SearchEngineModel.class);
	        //System.out.println(searchEngineInfo);
	        theToken = searchEngineInfo.getPayload().get("securityInfo").toString();
		    //System.out.println("Call search token value full:"+ theToken);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return theToken;

	}
	
	public JsonNode ParsingJsonStringIntoJsonNode(String jsonString) throws JsonParseException, IOException {
		//String jsonString = "{'k1':'v1','k2':'v2'}";
		ObjectMapper mapper = new ObjectMapper();
		JsonNode actualObj = mapper.readTree(jsonString);
		Assert.notNull(actualObj,"");
		return actualObj;
	}
}
